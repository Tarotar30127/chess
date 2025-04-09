package client;
import exception.ResponseException;
import model.AuthData;
import websocket.commands.*;


import java.io.*;
import java.net.URISyntaxException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerFacade {
    private final HttpCommunicator httpCommunicator;
    private final String serverUrl;
    WebSocketCommunicator webSocketCommunicator;
    String authToken;
    ServerMessageObserver notify;

    public void passinNotify(ServerMessageObserver notification){
        notify = notification;
        this.webSocketCommunicator = new WebSocketCommunicator(serverUrl, notify);
    }
    public ServerFacade(String url){
        serverUrl = url;
        this.httpCommunicator = new HttpCommunicator(url);
        this.authToken = null;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        return httpCommunicator.makeRequest(method, path, request, responseClass, authToken);
    }


    public AuthData register(String username, String password, String email) throws ResponseException {
        Map<String, String> body = Map.of(
                "username", username,
                "password", password,
                "email", email);
        AuthData resp = makeRequest("POST", "/user", body, AuthData.class);
        this.authToken = resp.authToken();
        return resp;
    }

    public AuthData login(String username, String password) throws ResponseException, IOException, URISyntaxException {
        Map<String, String> body = Map.of(
                "username", username,
                "password", password);
        AuthData resp = makeRequest("POST", "/session", body, AuthData.class);
        this.authToken = resp.authToken();
        return resp;
    }
    public Object logout(AuthData userauth) throws ResponseException {
        this.authToken = userauth.authToken();
        Object resp = makeRequest("DELETE", "/session", null, null);
        this.authToken = null;
        return resp;
    }

    public Object createGame(String gameName, AuthData userauth) throws ResponseException {
        this.authToken = userauth.authToken();
        Map<String, String> body = Map.of(
                "gameName", gameName);

        Object resp = makeRequest("POST", "/game", body, Object.class);
        return resp;
    }

    public Object playGame(String teamColor, int gameId, AuthData userauth) throws ResponseException {
        this.authToken = userauth.authToken();
        Map<String, Object> body = Map.of(
                "playerColor", teamColor,
                "gameID", gameId);

        Object resp = makeRequest("PUT", "/game", body, Object.class);
        return resp;
    }

    public Map observeGame(int gameId, AuthData userauth) throws ResponseException {
        this.authToken = userauth.authToken();
        Map games = makeRequest("GET", "/game", null, Map.class);
        Object gamesObj = games.get("games");
        List<Map<String, Object>> gamesList = (List<Map<String, Object>>) gamesObj;
        for (Map<String, Object> gameData : gamesList) {
            String gameDataStr = gameData.toString();
            Pattern pattern = Pattern.compile("gameID=(\\d+)");
            Matcher matcher = pattern.matcher(gameDataStr);
            if (matcher.find()) {
                int currentGameId = Integer.parseInt(matcher.group(1));
                if (currentGameId == gameId+1111) {
                    return gameData;
                }
            }
        }
        return Map.of(
                "Error", "Game does not exist");
    }

    public Map listGame(AuthData userauth) throws ResponseException {
        this.authToken = userauth.authToken();
        Map games = makeRequest("GET", "/game", null, Map.class);
        return games;
    }



    public void clear() throws ResponseException {
        Object resp = makeRequest("DELETE", "/db", null, Object.class);
    }



    public void joinPlayer(Connect command, ServerMessageObserver notify) throws ResponseException {
        this.webSocketCommunicator = new WebSocketCommunicator(serverUrl, notify);
        webSocketCommunicator.send(command);
    }

    public void joinObserver(Connect command, ServerMessageObserver notify) throws ResponseException {
        this.webSocketCommunicator = new WebSocketCommunicator(serverUrl, notify);
        webSocketCommunicator.send(command);
    }

    public void leave(Leave command) throws ResponseException {
        webSocketCommunicator.send(command);
    }

    public void resign(Resign command) throws ResponseException {
        webSocketCommunicator.send(command);
    }

    public void makeMove(Make_Move command) throws ResponseException {
        webSocketCommunicator.send(command);
    }
    public void redraw(Redraw command) throws ResponseException {
        webSocketCommunicator.send(command);
    }
}
