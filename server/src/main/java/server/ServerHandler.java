package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class ServerHandler {
    UserService userService;
    GameService gameService;

    public ServerHandler(UserService userService, GameService gameService){
        this.gameService = gameService;
        this.userService = userService;
    }

    public static Object joinGame(Request request, Response response) {
    }

    static Object createGame(Request request, Response response) {
    }

    static Object getGames(Request request, Response response) {
    }

    public static Object logoutUser(Request request, Response response) {
    }

    public static Object login(Request request, Response response) {

    }


    public static Object register(Request req, Response res) throws DataAccessException {
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        AuthData userAuthData = UserService.registerUser(user);
        res.status(200);
        return new Gson().toJson(userAuthData);

    }

}
