package ui;

import client.ServerFacade;
import exception.ResponseException;

import java.util.Scanner;

public class GameClient {
    private final Scanner scanner = new Scanner(System.in);
    private ServerFacade server;

    public GameClient(String serverUrl) {
        this.server = new ServerFacade(serverUrl);

    }

    public static String eval(String in) throws ResponseException {
        int number = Integer.parseInt(in.strip());
        return switch (number) {
            case 1 -> help();
            case 2 -> redraw();
            case 3 -> leave();
            case 4 -> makeMove();
            case 5 -> resign();
            case 6 -> highlight();
            default -> "Invalid command";
        };
    }

    private static String highlight() {
        return "";
    }

    private static String resign() {
        return "";
    }

    private static String makeMove() {
        return "";
    }

    private static String leave() {
        return "Exiting Game";
    }

    private static String redraw() {
        return "";
    }

    private static String help() {
        return """
            Help:
            - Help: Displays text informing the user what actions they can take.
            - Redraw Chess Board: Redraws the chess board upon the user’s request.
            - Leave: Removes the user from the game. The client transitions back to the Post-Login UI.
            - Make Move: Allows the user to input what move they want to make. The board is updated to reflect the result of the move, and the board automatically updates on all clients involved in the game.
            - Resign: Prompts the user to confirm they want to resign. If they do, the user forfeits the game and the game is over. Does not cause the user to leave the game.
            - Highlight Legal Moves: Allows the user to input the piece for which they want to highlight legal moves. The selected piece’s current square and all squares it can legally move to are highlighted. This is a local operation and has no effect on remote users’ screens.
            """;
    }
}
