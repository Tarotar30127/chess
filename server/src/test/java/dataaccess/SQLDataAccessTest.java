package dataaccess;
import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class SQLDataAccessTest {
    private static UserDAO userDAO;
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;

    @BeforeAll
    public static void setup() {
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();

    }
    @BeforeEach
    public void cleanSlate() throws ResponseException {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }
    @Test
    @DisplayName("User Clear Pass")
    public void userClearPass() throws ResponseException, SQLException, DataAccessException {
        UserData user1 = new UserData("tako", "legend", "@hotemail");
        UserData user2 = new UserData("tak", "legnd", "@hotemail");
        try {
            userDAO.createUser(user1);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        userDAO.createUser(user2);
        userDAO.clear();
        UserData user1Data = userDAO.getUser("tako");
        assertNull(user1Data);
    }
    @Test
    @DisplayName("Game Clear Pass")
    public void gameClearPass() throws ResponseException {
        gameDAO.createGame("newGame");
        gameDAO.createGame("funGame");
        gameDAO.clear();
        int gameId = 1112;
        GameData gameData = gameDAO.getGame(gameId);
        assertNull(gameData);
    }
    @Test
    @DisplayName("Auth Clear Pass")
    public void authClearPass() throws ResponseException {
        AuthData user1 = new AuthData("red","blue");
        AuthData user2 = new AuthData("green","yellow");
        try {
            authDAO.addAuth(user1);
            authDAO.addAuth(user2);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
        try {
            authDAO.clear();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
        AuthData authData = null;
        try {
            authData = authDAO.getAuth("red");
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
        assertNull(authData);
    }
    @Test
    @DisplayName("Update Players Pass")
    public void updatePlayersPass() throws ResponseException {
        int gameId = gameDAO.createGame("newGame");
        GameData newGame = new GameData(1112,"Player1", null, "newGame", new ChessGame());
        gameDAO.updatePlayers(newGame);
        GameData updatedGame = gameDAO.getGame(gameId);
        assertEquals("Player1", updatedGame.whiteUserName());
    }
    @Test
    @DisplayName("Update Players Fail")
    public void updatePlayersFail() throws ResponseException {
        assertThrows(RuntimeException.class, () -> gameDAO.updatePlayers(null));
    }
    @Test
    @DisplayName("Create Game Pass")
    public void createGamePass() throws ResponseException {
        int gameId = gameDAO.createGame("newGame");
        GameData gameData = gameDAO.getGame(gameId);
        assertNotNull(gameData);
        assertEquals("newGame", gameData.gameName());
    }
    @Test
    @DisplayName("Create Game Fail")
    public void createGameFail() {
        assertThrows(RuntimeException.class, () -> gameDAO.createGame(null));
    }
    @Test
    @DisplayName("List Games Pass")
    public void listGamesPass() throws ResponseException {
        gameDAO.createGame("game1");
        gameDAO.createGame("game2");
        Collection<GameData> games = gameDAO.listgames();
        assertNotNull(games);
        assertTrue(games.size() > 0);
    }
    @Test
    @DisplayName("List Games Fail")
    public void listGamesFail() throws ResponseException {
        gameDAO.clear();
        Collection<GameData> games = gameDAO.listgames();
        assertTrue(games.isEmpty());
    }
    @Test
    @DisplayName("Get Game Pass")
    public void getGamePass() throws ResponseException {
        int gameId = gameDAO.createGame("game1");
        GameData game = gameDAO.getGame(gameId);
        assertNotNull(game);
        assertEquals(gameId, game.gameId());
        assertEquals("game1", game.gameName());
    }
    @Test
    @DisplayName("Get Game Fail")
    public void getGameFail() throws ResponseException {
        GameData game = gameDAO.getGame(9999);
        assertNull(game);
    }
    @Test
    @DisplayName("Add Auth Pass")
    public void addAuthPass() throws ResponseException {
        AuthData authData = new AuthData("legend", "user1");
        AuthData result = authDAO.addAuth(authData);
        assertNotNull(result);
        assertEquals("user1", result.username());
        assertEquals("legend", result.authToken());
    }
    @Test
    @DisplayName("Add Auth Fail")
    public void addAuthFail() throws ResponseException {
        assertThrows(RuntimeException.class, () -> authDAO.addAuth(null));
    }
    @Test
    @DisplayName("Get Auth Pass")
    public void getAuthPass() throws ResponseException {
        AuthData authData = new AuthData("panda", "user1");
        authDAO.addAuth(authData);
        AuthData result = authDAO.getAuth("panda");
        assertNotNull(result);
        assertEquals("user1", result.username());
        assertEquals("panda", result.authToken());
    }
    @Test
    @DisplayName("Get Auth Fail")
    public void getAuthFail() throws ResponseException {
        AuthData result = authDAO.getAuth("tooCool");
        assertNull(result);
    }
    @Test
    @DisplayName("Delete Auth Pass")
    public void deleteAuthPositiveTest() throws ResponseException {
        AuthData authData = new AuthData("fox", "user1");
        authDAO.addAuth(authData);
        AuthData result = authDAO.deleteAuth("fox");
        assertNull(result);
    }

    @Test
    @DisplayName("Delete Auth Fail")
    public void deleteAuthNegativeTestNonExistingToken() throws ResponseException {
        AuthData result = authDAO.deleteAuth("dragonWarrior");
        assertNull(result);
    }




}
