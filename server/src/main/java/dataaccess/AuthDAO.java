package dataaccess;

import model.AuthData;

public interface AuthDAO {
    static void addAuth(AuthData authData);
    void getAuth(AuthData authToken);
    void deleteAuth(String authToken);
    void clear();

}
