package websocket.commands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{
    static ChessGame.TeamColor teamColor;
    public JoinPlayer(String authToken, Integer gameID, ChessGame.TeamColor teamColor) {
        super(CommandType.JOINPLAYER, authToken, gameID);
        this.teamColor = teamColor;
    }
    public static ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }
}

