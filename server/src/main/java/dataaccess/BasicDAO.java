package dataaccess;

import exception.ResponseException;

import java.sql.SQLException;

public class BasicDAO {
    protected void configureDatabase(String statement) throws ResponseException {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
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
                if (params[i] instanceof String p) {
                    ps.setString(i + 1, p);
                } else if (params[i] instanceof Integer p) {
                    ps.setInt(i + 1, p);
                } else if (params[i] == null) {
                    ps.setNull(i + 1, java.sql.Types.NULL);
                }
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                throw new ResponseException(403, "Duplication error");
            }
            throw new ResponseException(500, "SQL Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Database access error: " + e.getMessage());
        }
    }
}


