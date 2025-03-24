package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import exception.ResponseException;
import model.*;
import service.Service;
import spark.Request;
import spark.Response;

import java.util.*;

public class ServerHandler {
    private final Service userService;


    public ServerHandler(Service service){
        this.userService = service;
    }

    public Object joinGame(Request request, Response response) throws ResponseException {
        try {
            JoinColorId joinData = new Gson().fromJson(request.body(), JoinColorId.class);
            if (joinData.gameID() == null || joinData.playerColor() == null ||
                    !(joinData.playerColor().equals("WHITE") || joinData.playerColor().equals("BLACK"))) {
                throw new ResponseException(400, "Error: bad request");
            }
            String authToken = request.headers("authorization");
            JoinColorId joinedGame = userService.joinGame(joinData.gameID(), joinData.playerColor(), authToken);
            if (joinedGame == null){
                throw new ResponseException(403, "Error: already taken");
            }
            response.status(200);
            return "{}";
        } catch (JsonSyntaxException e) {
            throw new ResponseException(400, "Error: bad request");
        }

    }

    public Object createGame(Request request, Response response) throws ResponseException {
        String gameName = request.body();
        String authToken = request.headers("authorization");
        if (gameName == null) {
            throw new ResponseException(400, "Error: bad request");
        }
        JsonObject jsonGameName = JsonParser.parseString(gameName).getAsJsonObject();
        String justGameName = jsonGameName.get("gameName").getAsString();
        int gameId = userService.createGame(justGameName, authToken);
        response.status(200);
        return getJsonGameID(gameId);
    }

    private static String getJsonGameID(int gameID) {
        record JsonGameID(int gameID) {}
        return new Gson().toJson(new JsonGameID(gameID));
    }

    public Object getGames(Request request, Response response) throws ResponseException{
        try {
            String authToken = request.headers("authorization");
            Collection<GameData> games = userService.getGame(authToken);
            Collection<GamesList> gamesList = getJsonGames(games);
            response.status(200);
            Map<String, Object> formatedGameList = new HashMap<>();
            formatedGameList.put("games", gamesList);
            return new Gson().toJson(formatedGameList);
        } catch (Exception e) {
            throw new ResponseException(401, "Error: unauthorized");
        }
    }
    private static Collection<GamesList> getJsonGames(Collection<GameData> games) {
        ArrayList<GamesList> gamesList = new ArrayList<>();
        for (GameData game: games){
            GamesList gameList = new GamesList(game.gameId(), game.whiteUserName(), game.blackUserName(), game.gameName(), game.game());
            gamesList.add(gameList);
        }
        return gamesList;
    }

    public Object logoutUser(Request request, Response response) throws ResponseException {
        String authToken = request.headers("authorization");
        userService.logout(authToken);
        response.status(200);
        return "{}";
    }

    public Object login(Request request, Response response) throws ResponseException{
        UserData userData = new Gson().fromJson(request.body(), UserData.class);
        AuthData userAuthData = userService.loginUser(userData);
        response.status(200);
        return new Gson().toJson(userAuthData);
    }


    public Object register(Request req, Response response) throws ResponseException {
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        if (user.username()==null || user.password()==null || user.email()==null) {
            throw new ResponseException(400, "Error: bad request");
        }
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
}
