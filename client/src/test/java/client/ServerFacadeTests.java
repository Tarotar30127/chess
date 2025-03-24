package client;

import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import server.Server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
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
    public void registerPass() throws ResponseException {
        AuthData response = serverFacade.register("tako", "1234", "fun@hot.com");
        assertNotNull(response);
        assertNotNull(response.authToken());
    }

    @Test
    public void registerFail() throws ResponseException {
        AuthData response = serverFacade.register("failUser", "1234", "hot@mail");
        assertThrows(ResponseException.class, () -> serverFacade.register("failUser", "", ""));
    }

    @Test
    public void loginPass() throws ResponseException, IOException, URISyntaxException {
        AuthData response = serverFacade.register("funnyUser", "1234", "hot@mail");
        serverFacade.logout(response);
        AuthData responds = serverFacade.login("funnyUser", "1234");
        assertNotNull(responds);
        assertNotNull(responds.authToken());
    }

    @Test
    public void loginFail() {
        assertThrows(ResponseException.class, () -> serverFacade.login("funnyUser", "wrongPass"));
    }

    @Test
    public void logoutPass() throws ResponseException {
        AuthData response = serverFacade.register("funnyUser", "1234", "hot@mail");
        Object responds = serverFacade.logout(response);
        assertNull(responds);
    }

    @Test
    public void logoutFail() {
        assertThrows(ResponseException.class, () -> serverFacade.logout(new AuthData(null, null)));
    }

    @Test
    public void createGamePass() throws ResponseException {
        AuthData response = serverFacade.register("funnyUser", "1234", "hot@mail");
        Object responds = serverFacade.createGame("LOL Game", response);
        assertNotNull(responds);
    }

    @Test
    public void createGameFail() {
        assertThrows(ResponseException.class, () -> serverFacade.createGame("LOL Game", new AuthData(null, null)));
    }

    @Test
    public void playGamePass() throws ResponseException {
        AuthData response = serverFacade.register("funnyUser", "1234", "hot@mail");
        Object gameId = serverFacade.createGame("test", response);
        String gameIdStr = gameId.toString();
        Pattern pattern = Pattern.compile("gameID=(\\d+)");
        Matcher matcher = pattern.matcher(gameIdStr);
        if (matcher.find()) {
            int extractedGameId = Integer.parseInt(matcher.group(1));
            Object white = serverFacade.playGame("WHITE", extractedGameId, response);
            assertNotNull(white);
        }
    }

    @Test
    public void playGameFail() throws ResponseException {
        AuthData response = serverFacade.register("funnyUser", "1234", "hot@mail");
        assertThrows(ResponseException.class, () -> serverFacade.playGame("WHITE", -1, response));
    }

    @Test
    @Order(1)
    public void observeGamePass() throws ResponseException {
        AuthData response = serverFacade.register("failUser", "1234", "hot@mail");
        Object gameId = serverFacade.createGame("test", response);
        Object object = serverFacade.observeGame(1, response);
        assertNotNull(object);
    }

    @Test
    public void observeGameFail() throws ResponseException {
        AuthData response = serverFacade.register("failUser", "1234", "hot@mail");
        Map game= serverFacade.observeGame(-1, response);
        assertTrue(game.containsKey("Error"));
    }

    @Test
    public void listGamePass() throws ResponseException {
        AuthData response = serverFacade.register("failUser", "1234", "hot@mail");
        serverFacade.createGame("test1", response);
        serverFacade.createGame("random", response);
        Map games = serverFacade.listGame(response);
        assertNotNull(games);
        assertFalse(games.isEmpty());
    }

    @Test
    public void listGameFail() {
        assertThrows(ResponseException.class, () -> serverFacade.listGame(new AuthData(null, null)));
    }
}
