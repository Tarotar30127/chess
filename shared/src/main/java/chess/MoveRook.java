package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveRook extends moveTilEdge{
    @Override
    public Collection<ChessMove> PieceMoves(ChessBoard board, ChessPosition myPosition) {
        int [][] rookMovement = {{0,1},{0,-1},{1,0},{-1,0}};
        return super.moveEdge(board, myPosition, rookMovement);
    }
}
