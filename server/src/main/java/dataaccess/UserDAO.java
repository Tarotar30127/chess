package dataaccess;

import model.UserData;
import exception.ResponseException;

import java.sql.SQLException;

public interface UserDAO {

    void createUser(UserData userData) throws ResponseException, SQLException;
    UserData getUser(String userName) throws ResponseException;
    void clear() throws ResponseException;
}
