package chess;
import java.util.ArrayList;
import java.util.Collection;


public abstract class movePawn implements MovementCalculator {
    public Collection<ChessMove> moveOneSpace(ChessBoard board, ChessPosition myPosition, int[][] movement, int startingRow, int jumpRow, int promotionRow) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();

        // Check two-square move from starting position
        if (row == startingRow) {
            ChessPosition jump = new ChessPosition(jumpRow, col);
            ChessPosition front = new ChessPosition(row + movement[0][0], col);
            if (board.getPiece(front) == null && board.getPiece(jump) == null) {
                moves.add(new ChessMove(myPosition, jump, null));
            }
        }

        // Normal pawn movement & capturing
        for (int[] move : movement) {
            int r = row + move[0];
            int c = col + move[1];

            if (r < 1 || r > 8 || c < 1 || c > 8) {
                continue;
            }

            ChessPosition focus = new ChessPosition(r, c);
            ChessPiece occupied = board.getPiece(focus);

            // Forward movement (only if empty)
            if (col == c && occupied == null) {
                addMoveWithPromotion(moves, myPosition, focus, promotionRow);
            }

            // Capture move (only if opponent's piece is there)
            else if (col != c && occupied != null && occupied.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                addMoveWithPromotion(moves, myPosition, focus, promotionRow);
            }
        }
        return moves;
    }

    private void addMoveWithPromotion(Collection<ChessMove> moves, ChessPosition from, ChessPosition to, int promotionRow) {
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
