package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveWhitePawn extends movePawn{
    @Override
    public Collection<ChessMove> PieceMoves(ChessBoard board, ChessPosition myPosition) {
        int[][] movement = { { 1, -1 }, { 1, 0 }, { 1, 1 } };
        return moveOneSpace(board, myPosition, movement, 2, 4, 8);
    }
}
