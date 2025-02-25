package chess;


import java.util.Collection;

public class MoveBishop extends moveTilEdge{
    @Override
    public Collection<ChessMove> PieceMoves(ChessBoard board, ChessPosition myPosition){
        int[][] bishopMovement = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};
        return super.moveEdge(board, myPosition, bishopMovement);
    }
}
