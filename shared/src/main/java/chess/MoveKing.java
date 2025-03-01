package chess;

import java.util.Collection;

public class MoveKing extends moveOnce {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int [][] kingMovement = {{1,1},{-1,1},{1,-1},{-1,-1},{0,1},{0,-1},{1,0},{-1,0}};
        return super.moveOneSpot(board, myPosition, kingMovement);
    }
}
