package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import model.GameData;

import model.joinColorId;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Collection;
import java.util.Objects;

public class ServerHandler {
    private final UserService userService;


    public ServerHandler(UserService userService){
        this.userService = userService;
    }

    public Object joinGame(Request request, Response response) throws ResponseException {
        try {
            joinColorId joinData = new Gson().fromJson(request.body(), joinColorId.class);
            String authToken = request.headers("authorization");
            joinColorId joinedGame = userService.joinGame(joinData.gameID(), joinData.playerColor(), authToken);
            if (joinedGame == null){
                throw new ResponseException(403, "Error: already taken");
            }
            response.status(200);
            return "{}";
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    public Object createGame(Request request, Response response) throws ResponseException {
        String gameName = request.body();
        String authToken = request.headers("authorization");
        int gameID = userService.createGame(gameName, authToken);
        response.status(200);
        return new Gson().toJson(gameID);


    }

    public Object getGames(Request request, Response response) throws ResponseException{
        try {
            String authToken = request.headers("authorization");
            Collection<GameData> games = userService.getGame(authToken);
            response.status(200);
            return new Gson().toJson(games);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Object logoutUser(Request request, Response response) throws ResponseException {
        String authToken = request.headers("authorization");
        userService.logout(authToken);
        response.status(200);
        return "{}";
    }

    public Object login(Request request, Response response) throws ResponseException{
        UserData UserName = new Gson().fromJson(request.body(), UserData.class);
        AuthData userAuthData = userService.loginUser(UserName);
        response.status(200);
        return new Gson().toJson(userAuthData);
    }


    public Object register(Request req, Response response) throws ResponseException {
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        AuthData userAuthData = userService.registerUser(user);
        response.status(200);
        return new Gson().toJson(userAuthData);
    }

    public Object clear(Request request, Response response) {
        try {
            userService.clear();
            return "{}";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ServerHandler that)) {
            return false;
        }
        return Objects.equals(userService, that.userService);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userService);
    }
}
