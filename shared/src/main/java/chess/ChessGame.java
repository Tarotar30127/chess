package chess;

import javax.lang.model.util.SimpleElementVisitor6;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private boolean inCheckmate;
    private boolean stalemate;
    private ChessBoard board;

    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.inCheckmate = false;
        this.stalemate = false;
        board = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessGame chessGame)) {
            return false;
        }
        return inCheckmate == chessGame.inCheckmate && stalemate == chessGame.stalemate && teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, inCheckmate, stalemate, board);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                ", inCheckmate=" + inCheckmate +
                ", stalemate=" + stalemate +
                ", board=" + board +
                '}';
    }


    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece ==null){
            return null;
        }
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove move: possibleMoves){
            ChessBoard clonedBoard = board.clone();
            clonedBoard.addPiece(startPosition, null);
            clonedBoard.addPiece(move.getEndPosition(), piece.clone());
            ChessGame tempGame = new ChessGame();
            tempGame.board = clonedBoard;
            if (!tempGame.isInCheck(piece.getTeamColor())){
                validMoves.add(move);
            }
        }
        return validMoves;

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece movePiece = board.getPiece(move.getStartPosition());
        if (movePiece == null){
            throw new InvalidMoveException("No piece at the starting position.");
        }
        Collection<ChessMove> possibleMoves = validMoves(move.getStartPosition());
        boolean isValidMove = false;
        for (ChessMove possibleMove : possibleMoves) {
            if (move.getEndPosition().equals(possibleMove.getEndPosition())) {
                isValidMove = true;
                break;
            }
        }
        if (!isValidMove) {
            throw new InvalidMoveException("This is a Invalid Move");
        }
        if (movePiece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("It's not your turn.");
        }
        ChessBoard clonedBoard = board.clone();
        clonedBoard.addPiece(move.getStartPosition(), null);
        clonedBoard.addPiece(move.getEndPosition(), movePiece);
        ChessGame tempGame = new ChessGame();
        tempGame.board = clonedBoard;
        if (!tempGame.isInCheck(movePiece.getTeamColor())){
            if (move.getPromotionPiece()!=null) {
                board.addPiece(move.getEndPosition(), new ChessPiece(movePiece.getTeamColor(), move.getPromotionPiece()));
                board.addPiece(move.getStartPosition(), null);

            }
            else board.addPiece(move.getEndPosition(), movePiece);
            board.addPiece(move.getStartPosition(), null);
        }
        if (movePiece.getTeamColor() == TeamColor.BLACK){
            setTeamTurn(TeamColor.WHITE);
        }
        if (movePiece.getTeamColor() == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        }



    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king = null;
        for (int row = 1; row < 9; row++){
            for (int col = 1; col < 9; col++){
                ChessPosition lookAt = new ChessPosition(row, col);
                ChessPiece position = board.getPiece(lookAt);
                if (position != null){
                    if ((position.getPieceType() == ChessPiece.PieceType.KING)&&(position.getTeamColor() == teamColor)){
                        king = lookAt;
                        break;
                    }
                }
            }
        }
        for (int row = 1; row < 9; row++){
            for (int col = 1; col < 9; col++){
                ChessPosition focus = new ChessPosition(row, col);
                ChessPiece enemyPiece = board.getPiece(focus);
                if ((enemyPiece != null)&&(enemyPiece.getTeamColor() != teamColor)){
                    Collection<ChessMove> enemyMoves = enemyPiece.pieceMoves(board, focus);
                    for (ChessMove move: enemyMoves){
                        if (move.getEndPosition().equals(king)){
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return this.inCheckmate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return this.stalemate;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {

        return board;
    }
}
