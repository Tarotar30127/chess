package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    static void joinGame(Integer gameID, String s, String authToken) {
    }

    int createGame(String gameName);
    Collection<GameData> listgames();
    GameData getGame(int gameID);
    void clear();

}
