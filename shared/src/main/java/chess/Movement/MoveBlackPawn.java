package chess.Movement;


import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class MoveBlackPawn extends MovePawn {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int[][] movement = { { -1, 0 }, { -1, -1 }, { -1, 1 } };
        return moveOneSpace(board, myPosition, movement, 7, 5, 1);
    }
}
