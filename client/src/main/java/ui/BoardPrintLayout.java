package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.util.*;

import static ui.EscapeSequences.*;

public class BoardPrintLayout {
    static ChessGame currentGame;

    public BoardPrintLayout(ChessGame currentGame){
        this.currentGame = currentGame;
    }

    public static ChessGame getCurrentGame() {
        return currentGame;
    }

    public static void drawChessBoard(PrintStream out, ChessGame.TeamColor teamColor, ChessGame game) {
        currentGame = game;
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
            switch (focus.getPieceType()){
                case KING -> {
                    characterPiece = BLACK_KING;
                }
                case QUEEN -> {
                    characterPiece = BLACK_QUEEN;
                }
                case BISHOP -> {
                    characterPiece = BLACK_BISHOP;
                }
                case KNIGHT -> {
                    characterPiece = BLACK_KNIGHT;
                }
                case ROOK -> {
                    characterPiece = BLACK_ROOK;
                }
                case PAWN -> {
                    characterPiece = BLACK_PAWN;
                }
            }
            out.print(characterPiece);

        }
        else if (focus.getTeamColor() == ChessGame.TeamColor.WHITE){
            switch (focus.getPieceType()){
                case KING -> {
                    characterPiece = WHITE_KING;
                }
                case QUEEN -> {
                    characterPiece = WHITE_QUEEN;
                }
                case BISHOP -> {
                    characterPiece = WHITE_BISHOP;
                }
                case KNIGHT -> {
                    characterPiece = WHITE_KNIGHT;
                }
                case ROOK -> {
                    characterPiece = WHITE_ROOK;
                }
                case PAWN -> {
                    characterPiece = WHITE_PAWN;
                }

            }
            out.print(characterPiece);

        }
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

}


