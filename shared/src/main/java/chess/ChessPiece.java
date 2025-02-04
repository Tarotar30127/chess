package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        if (pieceColor == ChessGame.TeamColor.BLACK) {
            return pieceColor.BLACK;
        }
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            return pieceColor.WHITE;
        }
        return null;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return switch (type) {
            case PieceType.KING -> PieceType.KING;
            case PieceType.QUEEN -> PieceType.QUEEN;
            case PieceType.BISHOP -> PieceType.BISHOP;
            case PieceType.KNIGHT -> PieceType.KNIGHT;
            case PieceType.ROOK -> PieceType.ROOK;
            case PieceType.PAWN -> PieceType.PAWN;
            default -> null;
        };
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();
        PieceType pieceType = piece.getPieceType();
        ChessGame.TeamColor pieceColor = piece.getTeamColor();
        MovementCalculator calculate;
        switch (pieceType) {
            case PieceType.KING -> {
                moves = new MoveKing().PieceMoves(board, myPosition);
                return moves;
            }
            case PieceType.QUEEN -> {
                moves = new MoveQueen().PieceMoves(board, myPosition);
                return moves;
            }
            case PieceType.BISHOP -> {
                moves = new MoveBishop().PieceMoves(board, myPosition);
                return moves;
            }
            case PieceType.KNIGHT -> {
                moves = new MoveKnight().PieceMoves(board, myPosition);
                return moves;
            }
            case PieceType.ROOK -> {
                moves = new MoveRook().PieceMoves(board, myPosition);
                return moves;
            }
            case PieceType.PAWN -> {
                if (pieceColor == ChessGame.TeamColor.WHITE) {
                    moves = new MoveWhitePawn().PieceMoves(board, myPosition);
                }
                else {
                    moves = new MoveBlackPawn().PieceMoves(board, myPosition);
                }
                return moves;
            }
        }
       return moves;

    }
}