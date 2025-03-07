package dataaccess;

import exception.ResponseException;

import java.sql.SQLException;

public class BasicDAO {
    protected void configureDatabase(String[] statements) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            for (var state : statements) {
                try (var preparedStatement = conn.prepareStatement(state)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected void executeUpdate(String statement, Object... params) throws ResponseException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            for (var i = 0; i < params.length; i++) {
                if (params[i] instanceof String p) ps.setString(i + 1, p);
                else if (params[i] instanceof Integer p) ps.setInt(i + 1, p);
                else if (params[i] == null) ps.setNull(i + 1, java.sql.Types.NULL);
            }
            ps.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, String.format("Unable to update database: %s, %s", statement, e.getMessage()));
        }
    }
}


