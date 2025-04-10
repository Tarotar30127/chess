package ui;

import chess.*;
import client.ServerFacade;
import client.ServerMessageObserver;
import client.WebSocketCommunicator;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import websocket.commands.*;
import websocket.messages.LoadGame;
import websocket.messages.Notifcation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GameClient implements ServerMessageObserver{
    private static final Scanner SCANNER = new Scanner(System.in);
    private ServerFacade server;
    private WebSocketCommunicator ws;
    private AuthData userAuth;
    private int gameId;
    private ChessGame.TeamColor colorTeam;
    private boolean obsever;
    private BoardPrintLayout localChess;


    public GameClient(String serverUrl, AuthData auth, int gameID, ChessGame.TeamColor color)  {
        this.userAuth = auth;
        this.obsever= false;
        this.colorTeam = color;
        this.gameId = gameID;
        this.server = new ServerFacade(serverUrl);
        server.passinNotify(this);
        ws = new WebSocketCommunicator(serverUrl, this);
    }

    public void joinGame(int gameId, String authToken, ChessGame.TeamColor color, boolean isObserver) throws ResponseException {
        if (!isObserver) {
            try {
                server.joinPlayer(new Connect(authToken, gameId, color), this);
            } catch (ResponseException e) {
                throw new ResponseException(500, "unable to join as player");
            }
        } else {
            try {
                server.joinObserver(new Connect(authToken, gameId, null), this);
            } catch (ResponseException e) {
                throw new ResponseException(500, "unable to join as observer");
            }
        }
    }

    public String eval(String in, boolean observer) throws ResponseException {
        this.obsever = observer;
        int number = Integer.parseInt(in.strip());
        return switch (number) {
            case 1 -> help();
            case 2 -> redraw();
            case 3 -> leave();
            case 4 -> makeMove();
            case 5 -> resign();
            case 6 -> highlight();
            case 7 -> resetBoard();
            default -> "Invalid command";
        };
    }

    private String resetBoard() throws ResponseException {
        try {
            server.redraw(new Redraw(userAuth.authToken(), gameId));
        } catch (ResponseException e) {
            throw new ResponseException(403, "unable to draw");
        }
        return "";
    }

    private String highlight() throws ResponseException {
        ChessGame chessGame = localChess.getCurrentGame();
        System.out.println("Enter the Chess location of the piece you want to highlight legal moves for:");
        System.out.println("Enter the column (a-h) of the piece:");
        char startColChar = 0;
        int startCol = 0;
        try {
            startColChar = SCANNER.nextLine().strip().toLowerCase().charAt(0);

            Map<Character, Integer> charToNumMap = getCharToNumMap();;
            startCol = charToNumMap.get(startColChar);
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a letter from a to h.");
            return "Highlight canceled.";
        }

        System.out.println("Enter the row (1-8) of the piece:");
        int startRow;
        try {
            startRow = Integer.parseInt(SCANNER.nextLine().strip());
        } catch (NumberFormatException e) {
            System.out.println("Invalid row input. Please enter a number from 1 to 8.");
            return "Highlight canceled.";
        }

        if (startRow < 0 || startRow > 9 || startCol < 0 || startCol > 9) {
            return "Invalid position! Please enter a valid row (1-8) and column (a-h).";
        }
        ChessPosition startPosition = new ChessPosition(startRow, startCol);
        Collection<ChessMove> legalMoves = chessGame.validMoves(startPosition);
        if (legalMoves.isEmpty()) {
            return "No legal moves available for the selected piece!";
        }
        localChess.highlightMoves(legalMoves, colorTeam);
        System.out.println(legalMoves);
        return "Legal moves have been highlighted for the selected piece.";
    }

    private Map<Character, Integer> getCharToNumMap() {
        Map<Character, Integer> charToNumMap = new HashMap<>();
        if (colorTeam == ChessGame.TeamColor.WHITE) {
            for (char col = 'a'; col <= 'h'; col++) {
                charToNumMap.put(col, col - 'a' + 1);
            }
        } else if (colorTeam == ChessGame.TeamColor.BLACK) {
            for (char col = 'a'; col <= 'h'; col++) {
                charToNumMap.put(col, 9 - (col - 'a' + 1));
            }
        }
        return charToNumMap;
    }

    private String resign() throws ResponseException {
        if (obsever == true) {
            return "You are Observing!";
        }
        System.out.println("Are you sure you want to resign?\n");
        System.out.println("Enter Yes or No: ");
        String answer = SCANNER.nextLine();
        try {
            if (answer.toLowerCase().equals("yes")) {
                server.resign(new Resign(userAuth.authToken(), gameId, colorTeam));
                return "Good Game";
            }else {
                return "Good Luck! You can Win!";
            }
        } catch (ResponseException e) {
            System.out.println("Invalid answer. Please enter yes or no.");
            return "Resign canceled.";
        }
    }

    private String makeMove() throws ResponseException {
        if (obsever == true) {
            return "You are Observing!";
        }
        Map<Character, Integer> charToNumMap = getCharToNumMap();
        ChessPosition startPosition = null;
        try {
            System.out.println("""
                    Enter the Chess location of the piece you want to move
                    Enter the column (a-h) of the piece you want to move:
                    Example: a
                    Enter ->""");
            char startColChar = SCANNER.nextLine().strip().toLowerCase().charAt(0);
            System.out.println("""
                    Enter the row (1-8) of the piece you want to move:
                    Example: 1
                    Enter ->""");
            startPosition = getChessPosition(charToNumMap, startColChar);
        } catch (Exception e) {
            System.out.println("Invalid column or row input. Please enter a letter or number from a to h or 1 to 8.");
            return "Make Move canceled.";
        }
        if (startPosition == null) {
            return "invalid input";
        }
        int endRow = 0;
        int endCol = 0;
        ChessPosition endPosition = null;
        try {
            System.out.println("""
                    Enter the Chess location where you want to move the piece:
                    Enter the column (a-h) of where you want to move the piece:
                    Example: a
                    Enter ->""");
            char endColChar = SCANNER.nextLine().strip().toLowerCase().charAt(0);
            System.out.println("""
                    Enter the row (1-8) of where you want to move the piece:
                    Example: 4
                    Enter ->""");
            endRow = Integer.parseInt(SCANNER.nextLine().strip());
            endCol = charToNumMap.get(endColChar);
            endPosition = new ChessPosition(endRow, endCol);
        } catch (NumberFormatException e) {
            System.out.println("Invalid column or row input. Please enter a letter or number from a to h or 1 to 8.");
            return "Make Move canceled.";
        }
        if((endCol < 0)||(endCol>9)||(endRow < 0)||(endRow>9)){
            return "invalid input";
        }
        System.out.println("Do you want to promote a pawn? (yes/no)");
        String response = SCANNER.nextLine().strip().toLowerCase();
        ChessPiece.PieceType promotion = null;
        if (response.equals("yes")) {
            System.out.println("""
                    What would you like to promote it to: (Queen, Rook, Bishop, Knight)
                    Enter the piece ->""");
            String promotionInput = SCANNER.nextLine().strip().toLowerCase();
            switch (promotionInput) {
                case "queen" -> promotion = ChessPiece.PieceType.QUEEN;
                case "rook" -> promotion = ChessPiece.PieceType.ROOK;
                case "bishop" -> promotion = ChessPiece.PieceType.BISHOP;
                case "knight" -> promotion = ChessPiece.PieceType.KNIGHT;
                default -> {
                    System.out.println("Invalid promotion piece!");
                    return "Invalid promotion piece!";
                }
            }
        }
        ChessMove move = new ChessMove(startPosition, endPosition, promotion);
        server.makeMove(new MakeMove(userAuth.authToken(), gameId, move));
        return "Successfully submitted move";

    }

    private static ChessPosition getChessPosition (Map<Character, Integer> charToNumMap, char startColChar) {
        int startRow = Integer.parseInt(SCANNER.nextLine().strip());
        int startCol = charToNumMap.get(startColChar);
        ChessPosition startPosition = new ChessPosition(startRow, startCol);
        if((startCol < 0)||(startCol>9)||(startRow < 0)||(startRow>9)){
            return null;
        }
        return startPosition;
    }

    private String leave() throws ResponseException {
        try {
            server.leave(new Leave(userAuth.authToken(), gameId));
        } catch (ResponseException e) {
            throw new ResponseException(500, "unable to leave");
        }
        return "Exiting Game";
    }

    private String redraw() {
        localChess.printCurrentGame(colorTeam);
        return"";
    }

    private String help() {
        return """
            Help:
            - Help: Displays text informing the user what actions they can take.
            - Redraw Chess Board: Redraws the chess board upon the user’s request.
            - Leave: Removes the user from the game. The client transitions back to the Post-Login UI.
            - Make Move: Allows the user to input what move they want to make.\s
                    The board is updated to reflect the result of the move, and\s
                    the board automatically updates on all clients involved in the game.
            - Resign: Prompts the user to confirm they want to resign.\s
                    If they do, the user forfeits the game and the game is over.\s
                    Does not cause the user to leave the game.
            - Highlight Legal Moves: Allows the user to input the piece for which they want to highlight legal moves.\s
                        The selected piece’s current square and all squares it can legally move to are highlighted.\s
                        This is a local operation and has no effect on remote users’ screens.
           \s""";
    }

    @Override
    public void notify (String notification) {
        try {
            if (notification.contains("\"serverMessageType\":\"NOTIFICATION\"")) {
                Notifcation notify = new Gson().fromJson(notification, Notifcation.class);
                printNotification(notify.getMessage());
            } else if (notification.contains("\"serverMessageType\":\"ERROR\"")) {
                websocket.messages.Error error = new Gson().fromJson(notification, websocket.messages.Error.class);
                printError(error.getMessage());
            } else if (notification.contains("\"serverMessageType\":\"LOAD_GAME\"")) {
                LoadGame loadGame = new Gson().fromJson(notification, LoadGame.class);
                printGame(loadGame.returnGame());
            }
        } catch (Exception e) {
            System.out.println("Error processing the WebSocket message: " + e.getMessage());
        }
    }
    private void printGame(ChessGame chessGame) {
        BoardPrintLayout layout = new BoardPrintLayout(chessGame);
        localChess = layout;
        BoardPrintLayout.drawChessBoard(System.out, colorTeam, chessGame);
    }

    private void printError(String message) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Error: " + message + EscapeSequences.RESET_TEXT_COLOR);
    }

    private void printNotification(String message) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Notification: " + message + EscapeSequences.RESET_TEXT_COLOR);
    }
}

