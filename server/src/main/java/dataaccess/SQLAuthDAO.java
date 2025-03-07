package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SQLAuthDAO extends BasicDAO implements AuthDAO{
    private final String statement = """
            CREATE TABLE IF NOT EXISTS authdata (
                  `authtoken` varchar(256) NOT NULL PRIMARY KEY,
                  `username` varchar(256) NOT NULL
              )
            """;
    public SQLAuthDAO() {
        try {
            configureDatabase(statement);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthData addAuth(AuthData authData) {
        try {
            var statement = "INSERT INTO userdata (username, authtoken) VALUES (?, ?)";
            executeUpdate(statement, authData.username(), authData.authToken());
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authtoken, username FROM authdata WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readRs(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private AuthData readRs(ResultSet rs) {
        try {
            var authtoken = rs.getString("authtoken");
            var username = rs.getString("username");
            return new AuthData(authtoken, username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthData deleteAuth(String authToken) {
        var statement = "DELETE FROM pet WHERE id=?";
        try {
            executeUpdate(statement, authToken);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
        return getAuth(authToken);
    }

    @Override
    public void clear() {
        try {
            var statement = "DELETE FROM authdata";
            executeUpdate(statement);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }
}
