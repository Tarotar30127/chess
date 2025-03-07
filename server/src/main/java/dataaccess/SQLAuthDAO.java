package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;


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
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public AuthData deleteAuth(String authToken) {
        return null;
    }

    @Override
    public void clear() {

    }
}
