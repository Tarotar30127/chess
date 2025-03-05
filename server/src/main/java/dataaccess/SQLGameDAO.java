package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO{
    @Override
    public void updatePlayers(GameData existingGame) {

    }

    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public Collection<GameData> listgames() {
        return List.of();
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void clear() {

    }
}
