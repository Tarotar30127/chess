package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {
    private final String[] statement = { """
            CREATE TABLE IF NOT EXISTS  userdata (
               `id` int NOT NULL primary key AUTO_INCREMENT,
               `username` varchar(256) NOT NULL,
               `password` varchar( 256 ) NOT NULL,
               `email` varchar( 256 ) NOT NULL
             )
            """
    };
    public SQLUserDAO() throws ResponseException, DataAccessException {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    private void configureDatabase() throws DataAccessException, ResponseException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var state : statement) {
                try (var preparedStatement = conn.prepareStatement(state)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    @Override
    public void createUser(UserData userData) throws ResponseException, SQLException {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        var json = new Gson().toJson(userData);
        try {
            String sql = "INSERT INTO users (username, password, email) VALUES ('%s','%s','%s','%s')"
                    .formatted(userData.username(), userData.password(), userData.email());
            try (var stmt = conn.createStatement()) {
                int rowsAffected = stmt.executeUpdate(sql);
                if (rowsAffected > 0) {
                    System.out.println("ROW INSERTED");
                } else {
                    System.out.println("ROW NOT INSERTED");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        conn.close();
    }

    @Override
    public UserData getUser(String userName) throws ResponseException {
        return null;
    }

    @Override
    public void clear() throws ResponseException {

    }
}

