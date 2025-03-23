package client;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import spark.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.*;

public class ServerFacade {
    private String baseUrl;
    private String authToken;
    private Gson gson = new Gson();

    public ServerFacade(String serverDomain) {
        this.baseUrl = "http://" + serverDomain;
    }
    private Map request(String method, String endpoint, String body) throws ResponseException {
        try {
            HttpURLConnection http = makeConnection(method, endpoint, body);

            if (http.getResponseCode() == 401) {
                return Map.of("Error", 401);
            }
            try (InputStream respBody = http.getInputStream();
                 InputStreamReader reader = new InputStreamReader(respBody)) {
                return gson.fromJson(reader, Map.class);
            }
        } catch (URISyntaxException | IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    private HttpURLConnection makeConnection(String method, String endpoint, String body) throws URISyntaxException, IOException {
        URI uri = new URI(baseUrl + endpoint);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);
        if (authToken != null) {
            http.addRequestProperty("authorization", authToken);
        }
        if (body != null) {
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");
            http.getOutputStream().write(body.getBytes());
        }
        http.connect();
        return http;
    }

    public Object register(String username, String password, String email) throws ResponseException {
        Map<String, String> body = Map.of(
                "username", username,
                "password", password,
                "email", email);
        String jsonBody = gson.toJson(body);
        Map resp = request("POST", "/user", jsonBody);
        if (resp.containsKey("Error")) {
            return null;
        }
        authToken = (String) resp.get("authToken");
        return resp.toString();
    }

    public Object login(String username, String password) throws ResponseException {
        Map<String, String> body = Map.of(
                "username", username,
                "password", password);
        String jsonBody = gson.toJson(body);
        Map resp = request("POST", "/session", jsonBody);
        if (resp.containsKey("Error")) {
            return null;
        }
        authToken = (String) resp.get("authToken");
        return resp.toString();
    }
    public void logout() throws ResponseException {
        Map resp = request("POST", "/session", null);
        authToken = null;
    }

    public Object createGame() {
        return null;
    }

    public Object playGame() {
        return null;
    }

    public Object observeGame() {
        return null;
    }

}
