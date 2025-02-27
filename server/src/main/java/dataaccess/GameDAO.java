package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void updatePlayers(GameData existingGame);
    int createGame(String gameName);
    Collection<GameData> listgames();
    GameData getGame(int gameID);
    void clear();

}
