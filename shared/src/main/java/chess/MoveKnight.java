package chess;

import java.util.ArrayList;
import java.util.Collection;


public class MoveKnight implements MovementCalculator{
    @Override
    public Collection<ChessMove> PieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int [][] movement = {{2,1},{2,-1},{-2,-1},{-2,1},{1,2},{1,-2},{-1,-2},{-1,2}};
        for (int[] move : movement) {
            int r = row;
            int c = col;

            r += move[0];
            c += move[1];

            boolean baseCase = (r < 1) || (r > 8) || (c < 1) || (c > 8);
            if (baseCase == true) {
                continue;
            }
            ChessPosition focus = new ChessPosition(r, c);
            ChessPiece occupied = board.getPiece(focus);
            if (occupied != null) {
                if (occupied.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    moves.add(new ChessMove(myPosition, focus, null));
                }
                continue;
            }
            moves.add(new ChessMove(myPosition, focus, null));

        }
        return moves;

    }
}
