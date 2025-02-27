package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

import service.UserService;
import spark.Request;
import spark.Response;

public class ServerHandler {
    private final UserService userService;


    public ServerHandler(UserService userService){
        this.userService = userService;
    }

    public Object joinGame(Request request, Response response) {
        String gameName = request.params(":gameID");
        String authToken = request.headers("authToken");
    }

    public Object createGame(Request request, Response response) throws ResponseException {
        String gameName = request.params(":gameName");
        String authToken = request.headers("authToken");
        int gameID = userService.createGame(gameName, authToken);
        response.status(200);
        return new Gson().toJson(gameID);


    }

    public Object getGames(Request request, Response response) {

    }

    public Object logoutUser(Request request, Response response) throws ResponseException {
        String authToken = request.headers("authToken");
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
