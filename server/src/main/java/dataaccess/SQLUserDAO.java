package dataaccess;

import exception.ResponseException;
import model.UserData;

public class SQLUserDAO implements UserDAO{
    @Override
    public void createUser(UserData userData) throws ResponseException {

    }

    @Override
    public UserData getUser(String userName) throws ResponseException {
        return null;
    }

    @Override
    public void clear() throws ResponseException {

    }
}
