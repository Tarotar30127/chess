package server;

import dataaccess.*;

import exception.ResponseException;
import service.Service;
import spark.*;

public class Server {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    static ServerHandler serverHandler;


    public Server() {
        this.userDAO = new SQLUserDAO();
        this.authDAO = new SQLAuthDAO();
        this.gameDAO = new SQLGameDAO();
        Service service = new Service(userDAO, authDAO, gameDAO);
        serverHandler = new ServerHandler(service);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        // Register new user
        Spark.post("/user", serverHandler::register);
        // Login user
        Spark.post("/session", serverHandler::login);
        //Logout user
        Spark.delete("/session", serverHandler::logoutUser);
        // List Games
        Spark.get("/game", serverHandler::getGames);
        // Create game
        Spark.post("/game", serverHandler::createGame);
        // join game
        Spark.put("/game", serverHandler::joinGame);
        // clear app
        Spark.delete("/db", serverHandler::clear);

        //Spark Exception
        Spark.exception(ResponseException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.statusCode());
        res.body(ex.toJson());
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
