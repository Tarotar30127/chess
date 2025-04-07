package websocket.commands;

import chess.ChessMove;

public class Make_Move extends UserGameCommand{
    ChessMove move;
    public Make_Move(String authToken, Integer gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }
    public ChessMove getMove() {
        return move;
    }
}
