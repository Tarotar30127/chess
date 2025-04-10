package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.Service;
import websocket.commands.*;
import websocket.messages.Error;
import websocket.messages.LoadGame;
import websocket.messages.Notifcation;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebSocket
public class WebSocketHandler {
    private final ConnectionHandler connections = new ConnectionHandler();
    private static final Pattern COMMAND_TYPE_PATTERN = Pattern.compile("\"commandType\"\\s*:\\s*\"(\\w+)\"");
    private final Service service;

    public WebSocketHandler(Service service) {
        this.service = service;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws ResponseException, IOException {
        System.out.printf("Received: %s\n", msg);

        Matcher matcher = COMMAND_TYPE_PATTERN.matcher(msg);
        if (!matcher.find()) {
            System.out.println("Invalid command received");
            return;
        }

        String commandType = matcher.group(1);
        Gson gson = new Gson();

        switch (commandType) {
            case "CONNECT" -> {
                Connect command = gson.fromJson(msg, Connect.class);
                connect(session, command);
            }
            case "MAKE_MOVE" -> {
                Make_Move command = gson.fromJson(msg, Make_Move.class);
                makeMove(session, command);
            }
            case "LEAVE" -> {
                Leave command = gson.fromJson(msg, Leave.class);
                leaveGame(session, command);
            }
            case "RESIGN" -> {
                Resign command = gson.fromJson(msg, Resign.class);
                resign(session, command);
            }
            case "REDRAW" -> {
                Redraw command = gson.fromJson(msg, Redraw.class);
                redraw(session, command);
            }
            default -> System.out.println("Unknown command received " + commandType);
        }
    }

    private void redraw(Session session, Redraw command) throws ResponseException, IOException {
        String authToken = command.getAuthToken();
        AuthData auth = service.getAuthProfile(authToken);
        if (auth == null) {
            Error unauthorized = new Error("Invalid authToken.");
            ConnectionHandler.direct(unauthorized, session);
        }
        int gameId = command.getGameID();
        service.resetBoard(gameId);
        GameData gameData = service.getOneGame(gameId);
        LoadGame load = new LoadGame(gameData.game());
        ConnectionHandler.broadcast(load, gameId, session);
        ConnectionHandler.direct(load, session);
    }


    private void connect(Session session, Connect command) throws ResponseException, IOException {
        System.out.print(command);
        String authToken = command.getAuthToken();
        AuthData auth = service.getAuthProfile(authToken);
        if (auth == null) {
            Error unauthorized = new Error("Invalid authToken.");
            ConnectionHandler.direct(unauthorized, session);
            session.close();
            return;
        }
        int gameId = command.getGameID();
        GameData gameData = service.getOneGame(gameId);
        if (gameData == null) {
            Error gameNotFound = new Error("Game with ID " + gameId + " not found.");
            ConnectionHandler.direct(gameNotFound, session);
            session.close();
            return;
        }
        ChessGame.TeamColor teamColor = null;
        String serverMessage = null;
        if ((gameData.blackUserName()!=null && gameData.blackUserName().equals(auth.username()))
                || (gameData.blackUserName()!=null && gameData.whiteUserName().equals(auth.username()))) {
            if (gameData.whiteUserName()!=null && gameData.whiteUserName().equals(auth.username())) {
                teamColor = ChessGame.TeamColor.WHITE;
                serverMessage = String.format("%s has joined the game as %s".formatted(auth.username(), teamColor));
            }
            if (gameData.blackUserName()!=null && gameData.blackUserName().equals(auth.username())) {
                teamColor = ChessGame.TeamColor.BLACK;
                serverMessage = String.format("%s has joined the game as %s".formatted(auth.username(), teamColor));
            }
        } else {
            serverMessage = String.format("%s has joined the game as an observer".formatted(auth.username()));
        }
        connections.add(session, gameId, serverMessage);
        LoadGame load = new LoadGame(gameData.game());
        ConnectionHandler.direct(load, session);
    }



    private void error(Session session, websocket.messages.Error error) throws IOException {
        session.getRemote().sendString(new Gson().toJson(error));
    }




    private void resign(Session session, Resign command) throws ResponseException, IOException {
        String authToken = command.getAuthToken();
        AuthData auth = service.getAuthProfile(authToken);
        int gameId = command.getGameID();
        GameData gameData = service.getOneGame(gameId);
        if (!(auth.username().equals(gameData.whiteUserName()) || auth.username().equals(gameData.blackUserName()))) {
            var message = String.format("%s is not a player in the Game", auth.username());
            websocket.messages.Error notification = new websocket.messages.Error(message);
            error(session, notification);
            return;
        }

        if (gameData.game().checkGameOver()) {
            String message = "The game is already over";
            websocket.messages.Error errorMsg = new websocket.messages.Error(message);
            ConnectionHandler.direct(errorMsg, session);
            return;
       }

        String serverMessage = String.format("%s has resigned from the Game".formatted(auth.username()));
        ServerMessage msg = new Notifcation(serverMessage);
        ConnectionHandler.broadcast(msg, gameId, session);
        ConnectionHandler.direct(msg, session);
        gameData.game().setGameOver();
        GameData updatedGameData = new GameData(
                gameData.gameId(),
                gameData.whiteUserName(),
                gameData.blackUserName(),
                gameData.gameName(),
                gameData.game()

        );
        service.updateBoard(updatedGameData);

    }

    private void leaveGame(Session session, Leave command) throws ResponseException, IOException {
        String authToken = command.getAuthToken();
        AuthData auth = service.getAuthProfile(authToken);
        GameData gameData = service.getOneGame(command.getGameID());
        String whiteName = gameData.whiteUserName();
        String blackName = gameData.blackUserName();
        if (Objects.equals(gameData.whiteUserName(), auth.username())) {
            whiteName = null;
        }
        if (Objects.equals(gameData.blackUserName(), auth.username())) {
            blackName = null;
        }
        GameData updatedGameData = new GameData(
                gameData.gameId(),
                whiteName,
                blackName,
                gameData.gameName(),
                gameData.game()
        );
        service.updatePlayer(updatedGameData);
        String serverMessage = String.format("%s has left the game", auth.username());
        ServerMessage msg = new Notifcation(serverMessage);
        ConnectionHandler.broadcast(msg, command.getGameID(), session);
        connections.remove(session);
        session.close();
    }

    private void makeMove(Session session, Make_Move command) throws ResponseException, IOException {
        String authToken = command.getAuthToken();
        AuthData auth = service.getAuthProfile(authToken);
        if (auth == null) {
            Error unauthorized = new Error("Invalid authToken.");
            ConnectionHandler.direct(unauthorized, session);
            return;
        }
        int gameId = command.getGameID();
        GameData gameData = service.getOneGame(gameId);
        if (gameData == null) {
            Error gameNotFound = new Error("Game with ID " + gameId + " not found.");
            ConnectionHandler.direct(gameNotFound, session);
            return;
        }
        if (gameData.game().checkGameOver()){
            var message = String.format("The game is already over");
            Error notification = new Error(message);
            error(session, notification);
            return;
        }
        ChessGame.TeamColor playerColor = null;
        if (Objects.equals(auth.username(), gameData.whiteUserName())) {
            playerColor = ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(auth.username(), gameData.blackUserName())) {
            playerColor = ChessGame.TeamColor.BLACK;
        } else {
            var message = String.format("%s is an observer in the game", auth.username());
            Error notification = new Error(message);
            error(session, notification);
            return;
        }
        ChessGame game = gameData.game();
        ChessMove newMove = command.getMove();
        if (playerColor != game.getTeamTurn()) {
            var message = String.format("It is not %s turn.", auth.username());
            Error notification = new Error(message);
            error(session, notification);
            return;
        }
        try {
            game.makeMove(newMove);
        } catch (InvalidMoveException e) {
            var message = "Invalid move: " + e.getMessage();
            Error notification = new Error(message);
            error(session, notification);
            return;
        }
        boolean checkmate = game.isInCheckmate(game.getTeamTurn());
        boolean stalemate = game.isInStalemate(game.getTeamTurn());

        GameData updatedGameData = new GameData(
                gameId,
                gameData.whiteUserName(),
                gameData.blackUserName(),
                gameData.gameName(),
                game

        );
        service.updateBoard(updatedGameData);
        LoadGame load = new LoadGame(updatedGameData.game());
        ConnectionHandler.broadcast(load, gameId, session);
        ConnectionHandler.direct(load, session);
        var moveMessage = new Notifcation(auth.username() + " made a move.");
        ConnectionHandler.broadcast(moveMessage, gameId, session);
        if (checkmate) {
            var gameOverMsg = new Notifcation("Checkmate " + playerColor + " wins.");
            ConnectionHandler.broadcast(gameOverMsg, gameId, session);
            ConnectionHandler.direct(gameOverMsg, session);

        } else if (stalemate) {
            var gameOverMsg = new Notifcation("Stalemate The game is a draw.");
            ConnectionHandler.broadcast(gameOverMsg, gameId, session);
            ConnectionHandler.direct(gameOverMsg, session);

        }
    }


}
