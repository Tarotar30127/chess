package dataaccess;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


public class DataAccessTest {
    private static UserDAO userDAO;
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;

    @BeforeAll
    public static void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();

    }
    @BeforeEach
    public void cleanSlate() throws ResponseException {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }
    @Test
    @DisplayName("User Clear Pass")
    public void userClearPass() throws ResponseException {
        UserData user1 = new UserData("tako", "legend", "@hotemail");
        UserData user2 = new UserData("tak", "legnd", "@hotemail");
        userDAO.createUser(user1);
        userDAO.createUser(user2);
        userDAO.clear();
        UserData user1Data = userDAO.getUser("tako");
        assertNull(user1Data);
    }

}
