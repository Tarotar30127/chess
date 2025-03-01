package service;
import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.joinColorId;
import org.junit.jupiter.api.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class serviceTest {
    private static service service;
    private static UserDAO userDAO;
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;


    @BeforeAll
    public static void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        service = new service(userDAO, authDAO, gameDAO);
    }
    @BeforeEach
    public void cleanSlate() throws ResponseException {
        service.clear();
    }

    @Test
    @DisplayName("Register User Pass")
    public void registerUserPass() throws ResponseException {
        UserData newUser = new UserData("tako", "legend", "@hotemail");
        AuthData  authData = service.registerUser(newUser);
        Assertions.assertEquals(authData.username(), newUser.username());
    }
    @Test
    @DisplayName("Register User Duplication Request")
    public void registerUserFailAlreadyTaken() throws ResponseException {
        UserData newUser = new UserData("tako", "legend", "@hotemail");
        UserData dupUser = new UserData("tako", "legend", "@hotemail");
        AuthData  ogAuthData = service.registerUser(newUser);
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> {
            service.registerUser(dupUser);
        });
        Assertions.assertEquals("Error: already taken", exception.getMessage());
    }
    @Test
    @DisplayName("Logout Pass")
    public void logoutPass() throws ResponseException {
        UserData newUser = new UserData("tako", "legend", "@hotemail");
        AuthData  authData = service.registerUser(newUser);
        assertDoesNotThrow(() -> {service.logout(authData.authToken());});
    }
    @Test
    @DisplayName("logout Fail Unauthorized")
    public void logoutFailUnauthorized() throws ResponseException {
        UserData newUser = new UserData("tako", "legend", "@hotemail");
        AuthData  authData = service.registerUser(newUser);
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> {
            service.logout(null);
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }
    @Test
    @DisplayName("Login Pass")
    public void loginPass() throws ResponseException {
        UserData newUser = new UserData("tako", "legend", "@hotemail");
        AuthData authData = service.registerUser(newUser);
        service.logout(authData.authToken());
        assertDoesNotThrow(() -> {service.loginUser(newUser);});
    }
    @Test
    @DisplayName("Login Fail Unauthorized")
    public void loginFailUnauthorized() throws ResponseException {
        UserData oldUser = new UserData("tako", "legend", "@hotemail");
        UserData User = new UserData("tako", "leg", "@hotemail");
        AuthData  authData = service.registerUser(oldUser);
        service.logout(authData.authToken());
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> {
            service.loginUser(User);
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }
    @Test
    @DisplayName("Create Game Pass")
    public void createGamePass() throws ResponseException {
        UserData newUser = new UserData("tako", "legend", "@hotemail");
        AuthData authData = service.registerUser(newUser);
        assertDoesNotThrow(() -> {service.createGame("newGame",authData.authToken());});
    }
    @Test
    @DisplayName("Login Fail Unauthorized")
    public void createGameUnauthorized() throws ResponseException {
        UserData newUser = new UserData("tako", "legend", "@hotemail");
        AuthData authData = service.registerUser(newUser);
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> {
            service.createGame("newGame", null);
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }
    @Test
    @DisplayName("Login Fail Bad Request")
    public void createGameBadRequest() throws ResponseException {
        UserData newUser = new UserData("tako", "legend", "@hotemail");
        AuthData authData = service.registerUser(newUser);
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> {
            service.createGame("", authData.authToken());
        });
        Assertions.assertEquals("Error: bad request", exception.getMessage());
    }
    @Test
    @DisplayName("Join Game Pass")
    public void joinGamePass() throws ResponseException {
        UserData newUser = new UserData("tako", "legend", "@hotemail");
        AuthData authData = service.registerUser(newUser);
        int gameID = service.createGame("newGame",authData.authToken());
        joinColorId newGame = service.joinGame(gameID, "WHITE", authData.authToken());
        Assertions.assertEquals(newGame, new joinColorId("WHITE", gameID));
    }
    @Test
    @DisplayName("Join Game Fail Unauthorized")
    public void joinGameUnauthorized() throws ResponseException {
        UserData newUser = new UserData("tako", "legend", "@hotemail");
        AuthData authData = service.registerUser(newUser);
        int gameID = service.createGame("newGame",authData.authToken());
        joinColorId newGame = service.joinGame(gameID, "WHITE", authData.authToken());
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> {
            service.joinGame(gameID, "WHITE", null);
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }
    @Test
    @DisplayName("Join Game Fail Bad Request")
    public void joinGameBadRequest() throws ResponseException {
        UserData newUser = new UserData("tako", "legend", "@hotemail");
        AuthData authData = service.registerUser(newUser);
        int gameID = service.createGame("newGame",authData.authToken());
        joinColorId newGame = service.joinGame(gameID, "WHITE", authData.authToken());
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> {
            service.joinGame(gameID, null, authData.authToken());
        });
        Assertions.assertEquals("Error: bad request", exception.getMessage());
    }
    @Test
    @DisplayName("Game List Pass")
    public void getGameListPass() throws ResponseException {
        UserData newUser = new UserData("tako", "legend", "@hotemail");
        AuthData authData = service.registerUser(newUser);
        service.createGame("newGame", authData.authToken());
        service.createGame("new", authData.authToken());
        service.createGame("n", authData.authToken());
        assertDoesNotThrow(() -> {Collection<GameData> games = service.getGame(authData.authToken());});
    }
    @Test
    @DisplayName("Game List Fail Unauthorized")
    public void getGameListFailUnauthorized() throws ResponseException {
        UserData newUser = new UserData("tako", "legend", "@hotemail");
        AuthData authData = service.registerUser(newUser);
        int gameID = service.createGame("newGame",authData.authToken());
        joinColorId newGame = service.joinGame(gameID, "WHITE", authData.authToken());
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> {
            service.getGame(null);
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

}
