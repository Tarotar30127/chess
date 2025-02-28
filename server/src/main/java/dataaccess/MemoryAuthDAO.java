package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO{
    final private Map<String, AuthData> authDataMap = new HashMap<>();

    @Override
    public AuthData addAuth(AuthData authData) {
        authDataMap.put(authData.authToken(), authData);
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authDataMap.get(authToken);
    }

    @Override
    public AuthData deleteAuth(String authToken) {
        return authDataMap.remove(authToken);
    }

    @Override
    public void clear() {
        authDataMap.clear();

    }
}
