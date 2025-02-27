package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData addAuth(AuthData authData);
    AuthData getAuth(String authToken);
    AuthData deleteAuth(String authToken);
    void clear();

}
