package dataaccess;

import exception.ResponseException;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO extends BasicDAO implements UserDAO {
    private final String[] statement = { """
            CREATE TABLE IF NOT EXISTS userdata (
                  `username` varchar(256) NOT NULL PRIMARY KEY,
                  `password` varchar(256) NOT NULL,
                  `email` varchar(256) NOT NULL
              )
            """
    };
    public SQLUserDAO() throws ResponseException, DataAccessException {
        configureDatabase(statement);
    }

    @Override
    public void createUser(UserData userData) throws ResponseException, SQLException {
        var statement = "INSERT INTO userdata (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }

    @Override
    public UserData getUser(String userName) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM userdata WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readRs(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
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
    public void clear() throws ResponseException {
        var statement = "DELETE FROM userdata";
        executeUpdate(statement);
    }

}

