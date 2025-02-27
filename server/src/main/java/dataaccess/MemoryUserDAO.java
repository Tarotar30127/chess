package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO{
    final private Map<String, UserData> userDataMap = new HashMap<>();
    
    @Override
    public UserData createUser(UserData userData) {
        userDataMap.put(userData.userName(), userData);
        return userData;
    }

    @Override
    public Collection<UserData> listUsers() {
        return userDataMap.values();
    }

    @Override
    public UserData getUser(String userName) {
        return userDataMap.get(userName);
    }

    @Override
    public void clear() {
        userDataMap.clear();
    }
}
