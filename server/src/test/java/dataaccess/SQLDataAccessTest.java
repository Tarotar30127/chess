package dataaccess;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

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


}
