package dataaccess;
import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    private int nextId = 0000;
    final private Map<Integer, GameData> gameDataMap = new HashMap<>();

    @Override
    public int createGame(String gameName) {
        ChessGame game = new ChessGame();
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        game.setBoard(board);
        GameData newGame = new GameData(nextId++, null, null, game);
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
