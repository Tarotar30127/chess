package server;

import dataaccess.AuthDAO;

import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.UserService;
import spark.*;

public class Server {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    static ServerHandler serverHandler;

    public Server(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        serverHandler = new ServerHandler(userService);
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



        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }


    


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
