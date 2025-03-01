package dataaccess;
import chess.ChessGame;
import model.GameData;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    private int nextId = 1111;
    final private Map<Integer, GameData> gameDataMap = new HashMap<>();

    @Override
    public void updatePlayers(GameData existingGame) {
        try {
            gameDataMap.remove(existingGame.gameId());
            gameDataMap.put(existingGame.gameId(), existingGame);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int createGame(String gameName) {
        ChessGame game = new ChessGame();
        nextId++;
        GameData newGame = new GameData(nextId, null, null, gameName,game);
        gameDataMap.put(nextId, newGame);
        return nextId;
    }

    @Override
    public Collection<GameData> listgames() {
         return gameDataMap.values();
    }

    @Override
    public GameData getGame(int gameID) {
        return gameDataMap.get(gameID);
    }

    @Override
    public void clear() {
        gameDataMap.clear();
    }
}
