package dataaccess;

import exception.ResponseException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void updatePlayers(GameData existingGame);
    int createGame(String gameName) throws ResponseException;
    Collection<GameData> listgames() throws ResponseException;
    GameData getGame(int gameID) throws ResponseException;
    void clear() throws ResponseException;

}
