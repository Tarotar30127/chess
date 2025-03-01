package service;
import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

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


}
