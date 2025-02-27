package dataaccess;

import model.UserData;
import exception.ResponseException;

import java.util.Collection;

public interface UserDAO {

    UserData createUser(UserData userData) throws ResponseException;
    public Collection<UserData> listUsers() throws ResponseException;
    UserData getUser(String userName) throws ResponseException;
    void clear() throws ResponseException;

}
