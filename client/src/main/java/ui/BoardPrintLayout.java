package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.System.out;
import static ui.EscapeSequences.*;

public class BoardPrintLayout {
    ChessGame currentGame;

    public BoardPrintLayout(ChessGame currentGame){
        this.currentGame = currentGame;
    }

    private static Random rand = new Random();



    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        ChessGame game = new ChessGame();
        drawChessBoard(out, ChessGame.TeamColor.WHITE, game);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }



    private static void drawChessBoard(PrintStream out,ChessGame.TeamColor teamColor, ChessGame game) {
        List<String> headers = new ArrayList<>(Arrays.asList("", "A", "B", "C", "D", "E", "F", "G", "H", ""));
        List<String> fileLabel = new ArrayList<>(Arrays.asList(" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "));
        ChessBoard board = game.getBoard();
        if (teamColor == ChessGame.TeamColor.BLACK) {
            Collections.reverse(headers);
            Collections.reverse(fileLabel);
        }

        drawHeaders(headers);
        int counter = 1;
        for (String file:fileLabel){
            if (counter % 2 == 0) {
                drawEvenRow(file, Integer.parseInt(file.trim()), board);
            } else {
                drawOddRow(file, Integer.parseInt(file.trim()), board);
            }
            ++counter;

        }
        drawHeaders(headers);

    }

    private static void drawHeaders(List<String> headers) {
        setWhite(out);
        out.print("  ");
        for (int i = 0; i < 10; i++) {
            setWhite(out);
            printChar(headers.get(i));
            out.print(" ");
            if (i < 9) {
                setWhite(out);
                out.print(" ");
            }
            setBlack(out);
        }
        out.println();
    }
    private static void drawOddRow(String fileLabel, int row, ChessBoard board) {
        drawFileLabel(fileLabel);
        for (int col = 1; col <= 8; col++) {
            if (col % 2 == 0){
                out.print(SET_BG_COLOR_WHITE);
                out.print(SET_TEXT_COLOR_BLACK);
            }
            if (col % 2 == 1){
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(SET_TEXT_COLOR_BLACK);
            }
            placePiece(row, col, board);

            setBlack(out);
        }
        drawFileLabel(fileLabel);
        out.println();
    }

    private static void drawEvenRow(String fileLabel, int row, ChessBoard board) {
        drawFileLabel(fileLabel);
        for (int col = 1; col <= 8; col++) {
            if (col % 2 == 0){
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(SET_TEXT_COLOR_BLACK);
            }
            if (col % 2 == 1){
                out.print(SET_BG_COLOR_WHITE);
                out.print(SET_TEXT_COLOR_BLACK);
            }
            placePiece(row, col, board);

            setBlack(out);
        }
        drawFileLabel(fileLabel);
        out.println();
        }

    private static void drawFileLabel(String fileLabel) {
        setWhite(out);
        printChar(fileLabel);
        setBlack(out);
    }

    private static void placePiece(int row, int col, ChessBoard board){
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
                   characterPiece = EscapeSequences.BLACK_KING;
                }
                case QUEEN -> {
                    characterPiece = EscapeSequences.BLACK_QUEEN;
                }
                case BISHOP -> {
                    characterPiece = EscapeSequences.BLACK_BISHOP;
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
                    characterPiece = EscapeSequences.WHITE_KING;
                }
                case QUEEN -> {
                    characterPiece = EscapeSequences.WHITE_QUEEN;
                }
                case BISHOP -> {
                    characterPiece = EscapeSequences.WHITE_BISHOP;
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

    private static void printChar(String Char) {
    out.print(SET_BG_COLOR_WHITE);
    out.print(SET_TEXT_COLOR_BLACK);

    out.print(Char);

    setWhite(out);
}

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }
}

