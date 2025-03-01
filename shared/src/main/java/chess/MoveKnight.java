package chess;


import java.util.Collection;


public class MoveKnight extends moveOnce{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int [][] knightMovement = {{2,1},{2,-1},{-2,-1},{-2,1},{1,2},{1,-2},{-1,-2},{-1,2}};
        return super.moveOneSpot(board, myPosition, knightMovement);

    }
}
