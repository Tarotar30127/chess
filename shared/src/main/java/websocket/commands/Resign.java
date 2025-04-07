package websocket.commands;

import chess.ChessGame;

public class Resign extends UserGameCommand{
    static ChessGame.TeamColor teamColor;
    public Resign(String authToken, Integer gameID, ChessGame.TeamColor teamColor) {
        super(CommandType.RESIGN, authToken, gameID);
        this.teamColor = teamColor;
    }
    public static ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }
}
