package dataaccess;

import model.UserData;
import exception.ResponseException;

public interface UserDAO {

    void createUser(UserData userData) throws ResponseException;
    UserData getUser(String userName) throws ResponseException;
    void clear() throws ResponseException;
}
