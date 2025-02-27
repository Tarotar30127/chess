package server;

import com.google.gson.Gson;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

import service.UserService;
import spark.Request;
import spark.Response;

import javax.lang.model.element.NestingKind;

public class ServerHandler {
    private final UserService userService;


    public ServerHandler(UserService userService){
        this.userService = userService;
    }

    public Object joinGame(Request request, Response response) {
        record joinColorId(String playerColor, Integer gameID) {}
        joinColorId joinData = new Gson().fromJson(request.body(), joinColorId.class);
        String authToken = request.headers("authorization");
        GameDAO.joinGame(joinData.gameID, joinData.playerColor(), authToken);
        response.status(200);
        return null;

    }

    public Object createGame(Request request, Response response) throws ResponseException {
        String gameName = request.body();
        String authToken = request.headers("authorization");
        int gameID = userService.createGame(gameName, authToken);
        response.status(200);
        return new Gson().toJson(gameID);


    }

    public Object getGames(Request request, Response response) {

    }

    public Object logoutUser(Request request, Response response) throws ResponseException {
        String authToken = request.headers("authorization");
        userService.logout(authToken);
        response.status(200);
        return null;
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


}
