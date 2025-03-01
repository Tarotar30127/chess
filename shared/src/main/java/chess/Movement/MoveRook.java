package chess.Movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class MoveRook extends MoveTilEdge {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int [][] rookMovement = {{0,1},{0,-1},{1,0},{-1,0}};
        return super.moveEdge(board, myPosition, rookMovement);
    }
}
