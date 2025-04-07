package websocket.commands;

import chess.ChessGame;

public class Connect extends UserGameCommand{
    static ChessGame.TeamColor teamColor;
    public Connect(String authToken, Integer gameID, ChessGame.TeamColor teamColor) {
        super(CommandType.CONNECT, authToken, gameID);
        this.teamColor = teamColor;
    }
    public static ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }
}
