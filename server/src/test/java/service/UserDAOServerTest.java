package service;

import dataaccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOServerTest {
    private MemoryUserDAO userDAO;

    @BeforeAll
    static void startServer() {
        MemoryUserDAO userDAO = new MemoryUserDAO();
    }

   @Test
    void createUser() {
        var user = new UserData("car", "12345", "game300127@gmail.com");
        var result = assertDoesNotThrow(() -> userDAO.createUser(user));
        assertUserEqual(user, result);
   }

    @Test
    void getUser() {
        var user1 = new UserData("car", "12345", "game300127@gmail.com");
        var user2 = new UserData("red", "pika", "game30127@gmail.com");
        var user3 = new UserData("blue", "chu", "game3027@gmail.com");

        userDAO.createUser(user1);
        userDAO.createUser(user2);
        userDAO.createUser(user3);

        var result = assertDoesNotThrow(() -> userDAO.getUser("red"));
        assertNotNull(result, "User not found");
        assertUserEqual(user2, result);
    }

    @Test
    void listUsers() {
        var user1 = new UserData("car", "12345", "game300127@gmail.com");
        var user2 = new UserData("red", "pika", "game30127@gmail.com");
        var user3 = new UserData("blue", "chu", "game3027@gmail.com");

        userDAO.createUser(user1);
        userDAO.createUser(user2);
        userDAO.createUser(user3);

        Collection<UserData> users = userDAO.listUsers();
        assertEquals(2, users.size(), "There should be two users in the database.");
    }

    @Test
    void clearUsers() {
        var user1 = new UserData("car", "12345", "game300127@gmail.com");
        var user2 = new UserData("red", "pika", "game30127@gmail.com");
        var user3 = new UserData("blue", "chu", "game3027@gmail.com");

        userDAO.createUser(user1);
        userDAO.createUser(user2);
        userDAO.createUser(user3);

        userDAO.clear();
        assertTrue(userDAO.listUsers().isEmpty(), "Database should be empty after clear()");
    }


    public static void assertUserEqual(UserData expected, UserData actual) {
        assertEquals(expected.username(), actual.username());
        assertEquals(expected.password(), actual.password());
    }
}