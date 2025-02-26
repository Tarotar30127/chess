package dataaccess;

import model.GameData;

public interface GameDAO {
    void createGame(GameData gameData);
    void getGame(int gameID);
    void clear();

}
