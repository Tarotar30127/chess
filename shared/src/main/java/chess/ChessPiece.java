package chess;

import chess.movement.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable{
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

    @Override
    public ChessPiece clone() {
        try {
            ChessPiece clone = (ChessPiece) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
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
            return ChessGame.TeamColor.BLACK;
        }
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            return ChessGame.TeamColor.WHITE;
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
        switch (pieceType) {
            case PieceType.KING -> {
                moves = new MoveKing().pieceMoves(board, myPosition);
                return moves;
            }
            case PieceType.QUEEN -> {
                moves = new MoveQueen().pieceMoves(board, myPosition);
                return moves;
            }
            case PieceType.BISHOP -> {
                moves = new MoveBishop().pieceMoves(board, myPosition);
                return moves;
            }
            case PieceType.KNIGHT -> {
                moves = new MoveKnight().pieceMoves(board, myPosition);
                return moves;
            }
            case PieceType.ROOK -> {
                moves = new MoveRook().pieceMoves(board, myPosition);
                return moves;
            }
            case PieceType.PAWN -> {
                if (pieceColor == ChessGame.TeamColor.WHITE) {
                    moves = new MoveWhitePawn().pieceMoves(board, myPosition);
                }
                else {
                    moves = new MoveBlackPawn().pieceMoves(board, myPosition);
                }
                return moves;
            }
        }
       return moves;

    }
}