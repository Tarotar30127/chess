package chess.Movement;
import chess.*;

import java.util.ArrayList;
import java.util.Collection;


public abstract class MovePawn implements MovementCalculator {
    public Collection<ChessMove> moveOneSpace(ChessBoard board, ChessPosition myPosition,
                                              int[][] movement, int startingRow,
                                              int jumpRow, int promotionRow) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();

        if (row == startingRow) {
            ChessPosition jump = new ChessPosition(jumpRow, col);
            ChessPosition front = new ChessPosition(row + movement[0][0], col);
            if (board.getPiece(front) == null && board.getPiece(jump) == null) {
                moves.add(new ChessMove(myPosition, jump, null));
            }
        }

        for (int[] move : movement) {
            int r = row + move[0];
            int c = col + move[1];

            if (r < 1 || r > 8 || c < 1 || c > 8) {
                continue;
            }

            ChessPosition focus = new ChessPosition(r, c);
            ChessPiece occupied = board.getPiece(focus);

            if (col == c && occupied == null) {
                addPromotion(moves, myPosition, focus, promotionRow);
            }

            else if (col != c && occupied != null && occupied.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                addPromotion(moves, myPosition, focus, promotionRow);
            }
        }
        return moves;
    }

    private void addPromotion(Collection<ChessMove> moves, ChessPosition from, ChessPosition to, int promotionRow) {
        if (to.getRow() == promotionRow) {
            moves.add(new ChessMove(from, to, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(from, to, ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(from, to, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(from, to, ChessPiece.PieceType.BISHOP));
        } else {
            moves.add(new ChessMove(from, to, null));
        }
    }
}
