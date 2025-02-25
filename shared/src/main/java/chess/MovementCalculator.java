package chess;



import java.util.Collection;

public interface MovementCalculator {
    Collection<ChessMove> PieceMoves(ChessBoard board, ChessPosition myPosition);
}
