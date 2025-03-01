package chess.Movement;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public abstract class moveTilEdge implements MovementCalculator {
    public Collection<ChessMove> moveEdge(ChessBoard board, ChessPosition myPosition, int[][] movement) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();

        for (int[] move : movement) {
            int r = row;
            int c = col;
            while (true) {
                r += move[0];
                c += move[1];

                if (r < 1 || r > 8 || c < 1 || c > 8) {
                    break;
                }

                ChessPosition focus = new ChessPosition(r, c);
                ChessPiece occupied = board.getPiece(focus);

                if (occupied != null) {
                    if (occupied.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        moves.add(new ChessMove(myPosition, focus, null));
                    }
                    break;
                }
                moves.add(new ChessMove(myPosition, focus, null));
            }
        }
        return moves;
    }
}
