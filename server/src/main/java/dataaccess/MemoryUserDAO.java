package dataaccess;

import exception.ResponseException;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO{
    final private Map<String, UserData> userDataMap = new HashMap<>();
    
    @Override
    public void createUser(UserData userData) throws ResponseException {
        if (userDataMap.containsKey(userData.username())) {
            throw new ResponseException(403, "Error: already taken");
        }
        userDataMap.put(userData.username(), userData);
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
