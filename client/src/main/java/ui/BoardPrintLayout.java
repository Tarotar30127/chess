package ui;

import chess.ChessGame;

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

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    // Padded characters.
    private static final String EMPTY = "   ";
    public static final String WHITE_KING = " ♔ ";
    public static final String WHITE_QUEEN = " ♕ ";
    public static final String WHITE_BISHOP = " ♗ ";
    public static final String WHITE_KNIGHT = " ♘ ";
    public static final String WHITE_ROOK = " ♖ ";
    public static final String WHITE_PAWN = " ♙ ";
    public static final String BLACK_KING = " ♚ ";
    public static final String BLACK_QUEEN = " ♛ ";
    public static final String BLACK_BISHOP = " ♝ ";
    public static final String BLACK_KNIGHT = " ♞ ";
    public static final String BLACK_ROOK = " ♜ ";
    public static final String BLACK_PAWN = " ♟ ";

    private static Random rand = new Random();

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        drawChessBoard(out, ChessGame.TeamColor.BLACK);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }



    private static void drawChessBoard(PrintStream out,ChessGame.TeamColor teamColor) {
        List<String> headers = new ArrayList<>(Arrays.asList("", "A", "B", "C", "D", "E", "F", "G", "H", ""));
        List<String> fileLabel = new ArrayList<>(Arrays.asList(" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "));
        if (teamColor == ChessGame.TeamColor.BLACK) {
            Collections.reverse(headers);
            Collections.reverse(fileLabel);
        }

        drawHeaders(headers);
        int counter = 1;
        for (String file:fileLabel){
            if (counter % 2 == 0) {
                drawEvenRow(file);
            } else {
                drawOddRow(file);
            }
            ++counter;

        }
        drawHeaders(headers);

    }

    private static void drawHeaders(List<String> headers) {
        int index = 0;
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
            setWhite(out);
            int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
            int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
            out.print(EMPTY.repeat(prefixLength));
            printChar(headers.get(index));
            out.print(EMPTY.repeat(suffixLength));
            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                setWhite(out);
                out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
            setBlack(out);
            index++;
        }
        out.println();
    }
    private static void drawOddRow(String fileLabel) {
        int BOARD_SIZE = BOARD_SIZE_IN_SQUARES-5;
        drawFileLabel(fileLabel);
        for (int boardCol = 0; boardCol < BOARD_SIZE; boardCol++) {
            setWhite(out);
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
            if (boardCol < BOARD_SIZE - 1) {
                setGrey(out);
                out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
            setBlack(out);
        }
       drawFileLabel(fileLabel);
       out.println();
    }

    private static void drawEvenRow(String fileLabel) {
        int BOARD_SIZE = BOARD_SIZE_IN_SQUARES - 5;
        drawFileLabel(fileLabel);
        for (int boardCol = 0; boardCol < BOARD_SIZE; boardCol++) {
           setGrey(out);
           out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
            if (boardCol < BOARD_SIZE - 1) {
               setWhite(out);
               out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
            setBlack(out);
        }


        drawFileLabel(fileLabel);
        out.println();
    }

    private static void drawFileLabel(String fileLabel) {
        setWhite(out);
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
        out.print(EMPTY.repeat(prefixLength));
        printChar(fileLabel);
        out.print(EMPTY.repeat(suffixLength));
        setBlack(out);
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
