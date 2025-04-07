package server;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.apache.spark.scheduler.OutputCommitCoordinator;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.sparkproject.jetty.proxy.ConnectHandler;
import service.Service;
import websocket.commands.*;
import websocket.messages.LoadGame;
import websocket.messages.ServerMessage;

import java.io.IOException;
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
            System.out.println("Invalid command received.");
            return;
        }

        String commandType = matcher.group(1);
        Gson gson = new Gson();

        switch (commandType) {
            case "JOIN_PLAYER" -> {
                Connect command = gson.fromJson(msg, Connect.class);
                connect(session, command);
            }
            case "JOIN_OBSERVER" -> {
                JoinObserver command = gson.fromJson(msg, JoinObserver.class);
                joinObserver(session, command);
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
        int gameId = command.getGameID();
        GameData gameData = service.getOneGame(gameId);
        if (ChessGame.TeamColor.WHITE == Connect.getTeamColor()){
            if (gameData.whiteUserName() != auth.username()){
                var message = String.format("%s is not a player in the Game", auth.username());
                var notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, message);
                error(session, notification);
            }
        }else if (ChessGame.TeamColor.BLACK == Connect.getTeamColor()){
            if (gameData.blackUserName() != auth.username()){
                var message = String.format("%s is not a player in the Game", auth.username());
                var notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, message);
                error(session, notification);
            }
        }
        connections.add(session, gameId);
        String serverMessage = String.format("%s has joined the game as %s".formatted(auth.username(), command.getTeamColor()));
        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, serverMessage );
        ConnectionHandler.broadcast(session, msg, gameId);
        LoadGame load = new LoadGame(gameData.game());
        ConnectionHandler.broadcast(session, load, gameId);
    }



    private void error(Session session, ServerMessage error) throws IOException {
        System.out.printf("Error: %s%n", new Gson().toJson(error));
        session.getRemote().sendString(new Gson().toJson(error));
    }


    private void joinObserver(Session session, JoinObserver command) throws ResponseException, IOException {
        String authToken = command.getAuthToken();
        AuthData auth = service.getAuthProfile(authToken);
        int gameId = command.getGameID();
        GameData gameData = service.getOneGame(gameId);
        connections.add(session, gameId);
        String serverMessage = String.format("%s has joined the game as an observer".formatted(auth.username()));
        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, serverMessage );
        ConnectionHandler.broadcast(session, msg, gameId);
        LoadGame load = new LoadGame(gameData.game());
        ConnectionHandler.broadcast(session, load, gameId);
    }



    private void resign(Session session, Resign command) {

    }

    private void leaveGame(Session session, Leave command) throws ResponseException, IOException {
        String authToken = command.getAuthToken();
        AuthData auth = service.getAuthProfile(authToken);
        connections.remove(session);
        String serverMessage = String.format("%s has left the game".formatted(auth.username()));
        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, serverMessage );
        ConnectionHandler.broadcast(session, msg, command.getGameID());
    }

    private void makeMove(Session session, Make_Move command) {
    }


}
