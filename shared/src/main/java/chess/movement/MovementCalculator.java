package chess.movement;



import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public interface MovementCalculator {
    Collection<ChessMove>pieceMoves(ChessBoard board, ChessPosition myPosition);
}
