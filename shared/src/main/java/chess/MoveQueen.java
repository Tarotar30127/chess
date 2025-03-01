package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveQueen extends moveTilEdge{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int [][] queenMovement = {{1,1},{-1,1},{1,-1},{-1,-1},{0,1},{0,-1},{1,0},{-1,0}};
        return super.moveEdge(board, myPosition, queenMovement);
    }
}
