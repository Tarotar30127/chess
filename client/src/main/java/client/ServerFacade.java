package client;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import java.net.URL;
import java.util.*;

public class ServerFacade {
    private final String serverUrl;
    String authToken;



    public ServerFacade(String url) {
        this.authToken = null;
        serverUrl = url;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.setRequestProperty("authorization", this.authToken);
            String reqData = new Gson().toJson(request);
            System.out.println(reqData);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
        else {
            http.setRequestProperty("authorization", this.authToken);
        }

    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
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
        System.out.println(this.authToken);
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

    public Object observeGame(int gameId, AuthData userauth) throws ResponseException, IOException, URISyntaxException {
        this.authToken = userauth.authToken();
        Collection games = makeRequest("GET", "/game", null, Collection.class);
        return null;
    }

    public Map listGame(AuthData userauth) throws ResponseException {
        this.authToken = userauth.authToken();
        Map games = makeRequest("GET", "/game", null, Map.class);
        return games;
    }

    public void clear() throws ResponseException {
        Object resp = makeRequest("DELETE", "/db", null, Object.class);
    }


}
