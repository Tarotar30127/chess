package dataaccess;

import exception.ResponseException;
import model.AuthData;

public interface AuthDAO {
    AuthData addAuth(AuthData authData) throws ResponseException;
    AuthData getAuth(String authToken) throws ResponseException;
    AuthData deleteAuth(String authToken) throws ResponseException;
    void clear() throws ResponseException;

}
