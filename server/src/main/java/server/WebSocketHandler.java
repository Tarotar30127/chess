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
    private boolean resign = false;

    public WebSocketHandler(Service service) {
        this.service = service;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws ResponseException, IOException {
        System.out.printf("Received: %s\n", msg);

        Matcher matcher = COMMAND_TYPE_PATTERN.matcher(msg);
        if (!matcher.find()) {
            System.out.println("Invalid command received.");
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
            default -> System.out.println("Unknown command received: " + commandType);
        }
    }


    private void connect(Session session, Connect command) throws ResponseException, IOException {
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
        if ((gameData.blackUserName().equals(auth.username())) || (gameData.whiteUserName().equals(auth.username()))) {
            if (gameData.whiteUserName().equals(auth.username())) {
                teamColor = ChessGame.TeamColor.WHITE;
                serverMessage = String.format("%s has joined the game as %s".formatted(auth.username(), teamColor));
            }
            if (gameData.blackUserName().equals(auth.username())) {
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
        System.out.printf("Error: %s%n", new Gson().toJson(error));
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
        String serverMessage = String.format("%s has resigned from the Game".formatted(auth.username()));
        ServerMessage msg = new Notifcation(serverMessage);
        ConnectionHandler.broadcast(msg, gameId, session);
        ConnectionHandler.direct(msg, session);
        resign = true;
//        LoadGame load = new LoadGame(gameData.game());
//        ConnectionHandler.broadcast(load, gameId, session);
//        service.resetBoard(gameId);
//        GameData newGame = service.getOneGame(gameId);
//        LoadGame secondLoad = new LoadGame(newGame.game());
//        ConnectionHandler.broadcast(secondLoad, gameId, session);

    }

    private void leaveGame(Session session, Leave command) throws ResponseException, IOException {
        String authToken = command.getAuthToken();
        AuthData auth = service.getAuthProfile(authToken);
        connections.remove(session);
        String serverMessage = String.format("%s has left the game".formatted(auth.username()));
        ServerMessage msg = new Notifcation(serverMessage);
        ConnectionHandler.broadcast(msg, command.getGameID(), session);
    }

    private void makeMove(Session session, Make_Move command) throws ResponseException, IOException {
        String authToken = command.getAuthToken();
        AuthData auth = service.getAuthProfile(authToken);
        if (resign == true){
            var message = String.format("%s has resigned from the game", auth.username());
            Error notification = new Error(message);
            error(session, notification);
            return;
        }

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
            var message = String.format("It's not %s's turn.", auth.username());
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
        if (checkmate) {
            var gameOverMsg = new Notifcation("Checkmate! " + playerColor + " wins.");
            ConnectionHandler.broadcast(gameOverMsg, gameId, session);
        } else if (stalemate) {
            var gameOverMsg = new Notifcation("Stalemate! The game is a draw.");
            ConnectionHandler.broadcast(gameOverMsg, gameId, session);
        }else{
            var moveMessage = new Notifcation(auth.username() + " made a move.");
            ConnectionHandler.broadcast(moveMessage, gameId, session);

        }
    }


}
