package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO extends BasicDAO implements GameDAO{
    private int nextId = 1111;
    private final String statement = """
            CREATE TABLE IF NOT EXISTS gameData (
                  `gameId` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                  `whiteUserName` varchar(256),
                  `blackUserName` varchar(256),
                  `gameName` varchar(256) NOT NULL,
                  chessGame text
              )
            """;
    public SQLGameDAO() {
        try {
            configureDatabase(statement);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void updatePlayers(GameData existingGame) {
        try {
            var statement = """
            UPDATE gameData
            SET whiteUserName = ?, blackUserName = ?
            WHERE gameId = ?
        """;
            executeUpdate(statement, existingGame.whiteUserName(), existingGame.blackUserName(), existingGame.gameId());
        } catch (ResponseException e) {
            throw new RuntimeException("Error updating player information", e);
        }
    }

    @Override
    public int createGame(String gameName) {
        ChessGame game = new ChessGame();
        nextId++;
        var statement = "INSERT INTO gameData (gameId, whiteUserName, blackUserName, gameName, chessGame) VALUES (?, ?, ?, ?, ?)";
        try {
            executeUpdate(statement, nextId++, null, null, gameName, game);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
        return nextId;
    }

    @Override
    public Collection<GameData> listgames() {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameId, whiteUserName, blackUserName, gameName, chessGame FROM gameData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readRs(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);        }
        return result;
    }

    @Override
    public GameData getGame(int gameID) {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameId, whiteUserName, blackUserName, gameName, chessGame FROM gameData WHERE gameId = ?";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readRs(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    private GameData readRs(ResultSet rs) {
        try {
            var gameId = rs.getInt("gameId");
            var whiteUserName = rs.getString("whiteUserName");
            var blackUserName = rs.getString("blackUserName");
            var gameName = rs.getString("gameName");
            var chessGameJson = rs.getString("chessGame");
            ChessGame chessGame = new Gson().fromJson(chessGameJson, ChessGame.class);
            return new GameData(gameId, whiteUserName, blackUserName, gameName, chessGame);
        } catch (SQLException e) {
            throw new RuntimeException("Error reading game data from ResultSet", e);
        }
    }

    @Override
    public void clear() {
        try {
            var statement = "DELETE FROM gameData";
            executeUpdate(statement);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }
}
