package server;

import dataaccess.DataAccessException;

import spark.*;
import java.util.UUID;

public class Server {


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        // Register new user
        Spark.post("/user", ServerHandler::register);
        // Login user
        Spark.post("/session", ServerHandler::login);
        //Logout user
        Spark.delete("/session", ServerHandler::logoutUser);
        // List Games
        Spark.get("/game", ServerHandler::getGames);
        // Create game
        Spark.post("/game", ServerHandler::createGame);
        // join game
        Spark.put("/game", ServerHandler::joinGame);



        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }


    

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
