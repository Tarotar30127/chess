package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SQLAuthDAO extends BasicDAO implements AuthDAO{
    private final String[] statement = { """
            CREATE TABLE IF NOT EXISTS authdata (
                  `authtoken` varchar(256) NOT NULL PRIMARY KEY,
                  `username` varchar(256) NOT NULL
              )
            """
    };
    public SQLAuthDAO() throws ResponseException{
        configureDatabase(statement);
    }

    @Override
    public AuthData addAuth(AuthData authData) throws ResponseException {
        try {
            var statement = "INSERT INTO userdata (username, authtoken) VALUES (?, ?)";
            executeUpdate(statement, authData.username(), authData.authToken());
        } catch (ResponseException e) {
            throw new ResponseException(500, "unable to create");
        }
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) throws ResponseException {
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
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private AuthData readRs(ResultSet rs) throws SQLException {
        var authtoken = rs.getString("authtoken");
        var username = rs.getString("username");
        return new AuthData(authtoken, username);
    }

    @Override
    public AuthData deleteAuth(String authToken) throws ResponseException {
        var statement = "DELETE FROM pet WHERE id=?";
        executeUpdate(statement, authToken);
        return getAuth(authToken);
    }

    @Override
    public void clear() throws ResponseException {
        var statement = "DELETE FROM authdata";
        executeUpdate(statement);
    }
}
