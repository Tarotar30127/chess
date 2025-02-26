package dataaccess;

import model.UserData;

public interface UserDAO {
    static void createUser(UserData userData);
    void getUser(String userName);
    void clear();

}
