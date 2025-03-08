package dataaccess;

import exception.ResponseException;
import model.UserData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO extends BasicDAO implements UserDAO {
    private final String statement = """
            CREATE TABLE IF NOT EXISTS userdata (
                  `username` varchar(256) NOT NULL PRIMARY KEY,
                  `password` varchar(256) NOT NULL,
                  `email` varchar(256) NOT NULL
              )
            """;
    public SQLUserDAO() {
        try {
            configureDatabase(statement);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(UserData userData) throws ResponseException {
        var statement = "INSERT INTO userdata (username, password, email) VALUES (?, ?, ?)";
        try {
            executeUpdate(statement, userData.username(), userData.password(), userData.email());
        } catch (ResponseException e) {
            if (e.getMessage().contains("Duplicate entry")) {  // ✅ Detect duplicate username
                throw new ResponseException(403, "Username already taken");
            }
            throw e;
        }
    }

    @Override
    public UserData getUser(String userName) {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM userdata WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readRs(rs);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private UserData readRs(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }

    @Override
    public void clear() {
        try {
            var statement = "DELETE FROM userdata";
            executeUpdate(statement);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

}

