package chess;



import java.util.Collection;

public interface MovementCalculator {
    Collection<ChessMove>pieceMoves(ChessBoard board, ChessPosition myPosition);
}
