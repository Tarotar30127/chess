package chess.movement;


import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class MoveBishop extends MoveTilEdge {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        int[][] bishopMovement = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};
        return super.moveEdge(board, myPosition, bishopMovement);
    }
}
