package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO extends BasicDAO implements GameDAO{
    private int nextId = 1111;
    private final String[] statement = { """
            CREATE TABLE IF NOT EXISTS gameData (
                  `gameId` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                  `whiteUserName` varchar(256),
                  `blackUserName` varchar(256),
                  `gameName` varchar(256) NOT NULL,
                  chessGame text
              )
            """
    };
    public SQLGameDAO() throws ResponseException, DataAccessException {
        configureDatabase(statement);
    }
    @Override
    public void updatePlayers(GameData existingGame) {

    }

    @Override
    public int createGame(String gameName) throws ResponseException {
        ChessGame game = new ChessGame();
        nextId++;
        var statement = "INSERT INTO gameData (gameId, whiteUserName, blackUserName, gameName, chessGame) VALUES (?, ?, ?, ?, ?)";
        executeUpdate(statement, nextId++, null, null, gameName, game);
        return nextId;
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
