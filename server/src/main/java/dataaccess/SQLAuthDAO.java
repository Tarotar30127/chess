package dataaccess;

import exception.ResponseException;
import model.AuthData;

public class SQLAuthDAO implements AuthDAO{
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
