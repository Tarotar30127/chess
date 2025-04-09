package ui;

import chess.*;

import java.io.PrintStream;
import java.util.*;

import static ui.EscapeSequences.*;

public class BoardPrintLayout {
    private final ChessGame currentGame;

    public BoardPrintLayout(ChessGame currentGame){
        this.currentGame = currentGame;
    }

    public ChessGame getCurrentGame() {
        return currentGame;
    }

    public void printCurrentGame(ChessGame.TeamColor color) {
        drawChessBoard(System.out, color, currentGame);
    }

    public static void drawChessBoard(PrintStream out, ChessGame.TeamColor teamColor, ChessGame game) {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            drawWhiteView(out, game);
        } else {
            drawBlackView(out, game);
        }
        out.print(RESET_TEXT_COLOR);
    }
    private static void drawWhiteView(PrintStream out, ChessGame game) {
        List<String> columns = Arrays.asList(" A ", " B ", " C ", " D ", " E ", " F ", " G ", " H ");
        drawHeaders(out, columns);
        ChessBoard board = game.getBoard();
        for (int row = 8; row >= 1; row--) {
            drawRow(out, board, row, columns);
        }
        drawHeaders(out, columns);
    }

    private static void drawBlackView(PrintStream out, ChessGame game) {
        List<String> columns = Arrays.asList(" H ", " G " , " F ", " E ", " D " , " C ", " B ", " A ");
        drawHeaders(out, columns);
        ChessBoard board = game.getBoard();
        for (int row = 1; row <= 8; row++) {
            drawRow(out, board, row, columns);
        }
        drawHeaders(out, columns);
    }

    private static void drawHeaders(PrintStream out, List<String> columns) {
        setWhite(out);
        out.print("   ");
        for (String col : columns) {
            out.print(col);
        }
        out.print("   ");
        out.print(RESET_BG_COLOR);
        out.println();
    }
    private static void drawRow(PrintStream out, ChessBoard board, int row, List<String> columns) {
        String rowLabel = String.valueOf(row);
        if (row % 2 == 1) {
            drawOddRow(out, rowLabel, row, board);
        } else {
            drawEvenRow(out, rowLabel, row, board);
        }
    }

    private static void drawOddRow(PrintStream out, String fileLabel, int row, ChessBoard board) {
        dup(out, fileLabel, row, board, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY);
    }

    private static void drawEvenRow(PrintStream out, String fileLabel, int row, ChessBoard board) {
        dup(out, fileLabel, row, board, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE);
    }

    private static void dup(PrintStream out, String fileLabel, int row, ChessBoard board, String color1, String color2) {
        drawFileLabel(out, fileLabel);
        for (int col = 1; col <= 8; col++) {
            if (col % 2 == 0) {
                out.print(color1);
            } else {
                out.print(color2);
            }
            out.print(SET_TEXT_COLOR_BLACK);
            placePiece(out, row, col, board);
        }
        drawFileLabel(out, fileLabel);
        out.println();
    }

    private static void drawFileLabel(PrintStream out, String fileLabel) {
        setWhite(out);
        out.print(" " + fileLabel + " ");
        out.print(RESET_BG_COLOR);

    }

    private static void placePiece(PrintStream out, int row, int col, ChessBoard board){
        ChessPosition location = new ChessPosition(row, col);
        ChessPiece focus = board.getPiece(location);
        if (focus == null) {
            out.print(EMPTY);
            return;
        }
        String characterPiece = null;
        if (focus.getTeamColor()== ChessGame.TeamColor.BLACK) {
            getPieceType(out, focus, characterPiece, BLACK_KING, BLACK_QUEEN, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK, BLACK_PAWN);

        }
        else if (focus.getTeamColor() == ChessGame.TeamColor.WHITE){
            getPieceType(out, focus, characterPiece, WHITE_KING, WHITE_QUEEN, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK, WHITE_PAWN);

        }
    }

    private static void getPieceType(PrintStream out, ChessPiece focus, String characterPiece, String whiteKing, String whiteQueen, String whiteBishop, String whiteKnight, String whiteRook, String whitePawn) {
        switch (focus.getPieceType()){
            case KING -> {
                characterPiece = whiteKing;
            }
            case QUEEN -> {
                characterPiece = whiteQueen;
            }
            case BISHOP -> {
                characterPiece = whiteBishop;
            }
            case KNIGHT -> {
                characterPiece = whiteKnight;
            }
            case ROOK -> {
                characterPiece = whiteRook;
            }
            case PAWN -> {
                characterPiece = whitePawn;
            }

        }
        out.print(characterPiece);
    }

    private static void printChar(PrintStream out, String s) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(s);
        setWhite(out);
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setGrey(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BoardPrintLayout that)) {
            return false;
        }
        return Objects.equals(currentGame, that.currentGame);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(currentGame);
    }

    public void highlightMoves(Collection<ChessMove> legalMoves, ChessGame.TeamColor color) {
        Set<ChessPosition> possibleMoves = new HashSet<>();
        ChessPosition startPosition = null;
        for (ChessMove move : legalMoves) {
            startPosition = move.getStartPosition();
            possibleMoves.add(move.getEndPosition());
        }

        ChessBoard board = currentGame.getBoard();
        PrintStream out = System.out;

        List<Integer> rowOrder = (color == ChessGame.TeamColor.WHITE)
                ? Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1)
                : Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

        List<Integer> colOrder = (color == ChessGame.TeamColor.WHITE)
                ? Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8)
                : Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1);

        List<String> colLabels = (color == ChessGame.TeamColor.WHITE)
                ? Arrays.asList(" A ", " B ", " C ", " D ", " E ", " F ", " G ", " H ")
                : Arrays.asList(" H ", " G ", " F ", " E ", " D ", " C ", " B ", " A ");

        drawHeaders(out, colLabels);

        for (int row : rowOrder) {
            setWhite(out);
            out.print(" " + row + " ");
            out.print(RESET_BG_COLOR);
            for (int col : colOrder) {
                ChessPosition pos = new ChessPosition(row, col);

                if (possibleMoves.contains(pos)) {
                    out.print(SET_BG_COLOR_YELLOW);
                } else if (startPosition.equals(pos)) {
                    out.print(SET_BG_COLOR_RED);
                } else if ((row + col) % 2 == 0) {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                } else {
                    out.print(SET_BG_COLOR_WHITE);
                }

                out.print(SET_TEXT_COLOR_BLACK);
                ChessPiece piece = board.getPiece(pos);
                if (piece == null) {
                    out.print(EMPTY);
                } else {
                    placePiece(out, row, col, board);
                }
            }
            setWhite(out);
            out.print(" " + row + " ");
            out.print(RESET_BG_COLOR);
            out.println();
        }

        drawHeaders(out, colLabels);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        
    }

}




