package dataaccess;

import exception.ResponseException;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO{
    final private Map<String, UserData> userDataMap = new HashMap<>();
    
    @Override
    public UserData createUser(UserData userData) throws ResponseException {
        if (userDataMap.containsKey(userData.username())) {
            throw new ResponseException(403, "Error: already taken");
        }
        userDataMap.put(userData.username(), userData);
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
