package client;

import exception.ResponseException;
import model.AuthData;
import model.GamesList;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import server.Server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static AuthData authData;

    @BeforeEach
    public void init() throws ResponseException, IOException, URISyntaxException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
        authData = serverFacade.login("funnyUser", "1234");
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }

    @Test
    public void register_methodPass() throws ResponseException {
        AuthData response = serverFacade.register("funnyUser2", "1234", "funnyEmail2@lol.com");
        assertNotNull(response);
        assertNotNull(response.authToken());
    }

    @Test
    public void register_methodFail() {
        // Assuming the server throws an exception on invalid data
        assertThrows(ResponseException.class, () -> serverFacade.register("failUser", "", ""));
    }

    @Test
    public void login_methodPass() throws ResponseException, IOException, URISyntaxException {
        AuthData response = serverFacade.login("funnyUser", "1234");
        assertNotNull(response);
        assertNotNull(response.authToken());
    }

    @Test
    public void login_methodFail() {
        // Trying to log in with invalid credentials
        assertThrows(ResponseException.class, () -> serverFacade.login("funnyUser", "wrongPass"));
    }

    @Test
    public void logout_methodPass() throws ResponseException {
        Object response = serverFacade.logout(authData);
        assertNotNull(response);
    }

    @Test
    public void logout_methodFail() {
        // Simulating a logout with invalid auth data
        assertThrows(ResponseException.class, () -> serverFacade.logout(new AuthData(null, null)));
    }

    @Test
    public void createGame_methodPass() throws ResponseException {
        Object response = serverFacade.createGame("LOL Game", authData);
        assertNotNull(response);
    }

    @Test
    public void createGame_methodFail() {
        // Invalid auth token, so the game creation should fail
        assertThrows(ResponseException.class, () -> serverFacade.createGame("LOL Game", new AuthData(null, null)));
    }

    @Test
    public void playGame_methodPass() throws ResponseException {
        Object response = serverFacade.playGame("WHITE", 1, authData);
        assertNotNull(response);
    }

    @Test
    public void playGame_methodFail() {
        // Simulating invalid game ID
        assertThrows(ResponseException.class, () -> serverFacade.playGame("WHITE", -1, authData));
    }

    @Test
    public void observeGame_methodPass() throws ResponseException, IOException, URISyntaxException {
        Object response = serverFacade.observeGame(1, authData);
        assertNotNull(response);
    }

    @Test
    public void observeGame_methodFail() {
        // Simulating invalid game ID
        assertThrows(ResponseException.class, () -> serverFacade.observeGame(-1, authData));
    }

    @Test
    public void listGame_methodPass() throws ResponseException {
        Collection<GamesList> games = serverFacade.listGame(authData);
        assertNotNull(games);
        assertFalse(games.isEmpty());
    }

    @Test
    public void listGame_methodFail() {
        // Invalid auth token, so listing games should fail
        assertThrows(ResponseException.class, () -> serverFacade.listGame(new AuthData(null, null)));
    }
}
