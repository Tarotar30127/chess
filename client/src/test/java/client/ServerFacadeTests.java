package client;

import exception.ResponseException;
import model.AuthData;
import org.apache.hadoop.yarn.webapp.hamlet2.HamletSpec;
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

    @BeforeAll
    public static void init() throws ResponseException, IOException, URISyntaxException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }
    @BeforeEach
    public void clear() throws ResponseException {
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() throws ResponseException {
        serverFacade.clear();
        server.stop();
    }

    @Test
    public void register_methodPass() throws ResponseException {
        AuthData response = serverFacade.register("tako", "1234", "fun@hot.com");
        assertNotNull(response);
        assertNotNull(response.authToken());
    }

    @Test
    public void register_methodFail() throws ResponseException {
        AuthData response = serverFacade.register("failUser", "1234", "hot@mail");
        assertThrows(ResponseException.class, () -> serverFacade.register("failUser", "", ""));
    }

    @Test
    public void login_methodPass() throws ResponseException, IOException, URISyntaxException {
        AuthData response = serverFacade.register("funnyUser", "1234", "hot@mail");
        serverFacade.logout(response);
        AuthData responds = serverFacade.login("funnyUser", "1234");
        assertNotNull(responds);
        assertNotNull(responds.authToken());
    }

    @Test
    public void login_methodFail() {
        assertThrows(ResponseException.class, () -> serverFacade.login("funnyUser", "wrongPass"));
    }

    @Test
    public void logout_methodPass() throws ResponseException {
        AuthData response = serverFacade.register("funnyUser", "1234", "hot@mail");
        Object responds = serverFacade.logout(response);
        assertNull(responds);
    }

    @Test
    public void logout_methodFail() {
        assertThrows(ResponseException.class, () -> serverFacade.logout(new AuthData(null, null)));
    }

    @Test
    public void createGame_methodPass() throws ResponseException {
        AuthData response = serverFacade.register("funnyUser", "1234", "hot@mail");
        Object responds = serverFacade.createGame("LOL Game", response);
        assertNotNull(responds);
    }

    @Test
    public void createGame_methodFail() {
        assertThrows(ResponseException.class, () -> serverFacade.createGame("LOL Game", new AuthData(null, null)));
    }

    @Test
    public void playGame_methodPass() throws ResponseException {
        AuthData response = serverFacade.register("funnyUser", "1234", "hot@mail");
        Object gameId = serverFacade.createGame("test", response);
        System.out.println(gameId.toString());
        Object white = serverFacade.playGame("WHITE", 1, response);
        assertNotNull(white);
    }

    @Test
    public void playGame_methodFail() throws ResponseException {
        AuthData response = serverFacade.register("funnyUser", "1234", "hot@mail");
        assertThrows(ResponseException.class, () -> serverFacade.playGame("WHITE", -1, response));
    }

    @Test
    public void observeGame_methodPass() throws ResponseException, IOException, URISyntaxException {
        AuthData response = serverFacade.register("failUser", "1234", "hot@mail");
        Object gameId = serverFacade.createGame("test", response);
        Object object = serverFacade.observeGame(1, response);
        assertNotNull(object);
    }

    @Test
    public void observeGame_methodFail() throws ResponseException {
        AuthData response = serverFacade.register("failUser", "1234", "hot@mail");
        Map game= serverFacade.observeGame(-1, response);
        assertTrue(game.containsKey("Error"));
    }

    @Test
    public void listGame_methodPass() throws ResponseException {
        AuthData response = serverFacade.register("failUser", "1234", "hot@mail");
        serverFacade.createGame("test1", response);
        serverFacade.createGame("random", response);
        Map games = serverFacade.listGame(response);
        assertNotNull(games);
        assertFalse(games.isEmpty());
    }

    @Test
    public void listGame_methodFail() {
        assertThrows(ResponseException.class, () -> serverFacade.listGame(new AuthData(null, null)));
    }
}
