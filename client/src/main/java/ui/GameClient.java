package ui;

import chess.*;
import client.ServerFacade;
import client.ServerMessageObserver;
import client.WebSocketCommunicator;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import websocket.commands.Leave;
import websocket.commands.Make_Move;
import websocket.commands.Resign;

import java.util.Map;
import java.util.Scanner;

public class GameClient {
    private static final Scanner scanner = new Scanner(System.in);
    private ServerFacade server;
    private final ServerMessageObserver serverMessageObserver;
    private WebSocketCommunicator ws;
    private AuthData userAuth;
    private int gameId;
    private ChessGame.TeamColor colorTeam;
    private ChessGame game;



    public GameClient(String serverUrl, ServerMessageObserver notify, AuthData auth, String gameID, ChessGame.TeamColor color, GameData currentGame) throws ResponseException {
        this.userAuth = auth;
        this.game = currentGame.game();
        this.colorTeam = color;
        this.gameId = Integer.parseInt(gameID.strip());
        this.server = new ServerFacade(serverUrl);
        this.serverMessageObserver = notify;
        ws = new WebSocketCommunicator(serverUrl, serverMessageObserver);
        this.userAuth = null;
    }

    public String eval(String in) throws ResponseException {
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

    private String highlight() {
        return "";
    }

    private String resign() throws ResponseException {
        System.out.println("Are you sure you want to resign?\n");
        System.out.println("Enter Yes or No: ");
        String answer = scanner.nextLine();
        if (answer.toLowerCase() == "yes") {
            server.resign(new Resign(userAuth.authToken(), gameId, colorTeam));
        }
        return "Good Luck! You can Win!";
    }

    private String makeMove() throws ResponseException {
        Map<Character, Integer> charToNumMap = Map.of(
                'a', 1,
                'b', 2,
                'c', 3,
                'd', 4,
                'e', 5,
                'f', 6,
                'g', 7,
                'h', 8
        );
        System.out.println("""
                Enter the Chess location of the piece you want to move
                Enter the row (1-8) of the piece you want to move:
                Example: 1
                Enter ->
                """);
        int startRow = Integer.parseInt(scanner.nextLine().strip());
        System.out.println("""
                Enter the column (a-h) of the piece you want to move:
                Example: a
                Enter ->
                """);
        char startColChar = scanner.nextLine().strip().toLowerCase().charAt(0);
        if (startRow < 1 || startRow > 8 || startColChar < 'a' || startColChar > 'h') {
            return "Invalid starting position! Please enter a valid row (1-8) and column (a-h)";
        }

        Integer startCol = charToNumMap.get(startColChar);
        ChessPosition startPosition = new ChessPosition(startRow, startCol);
        System.out.println("""
                Enter the Chess location where you want to move the piece:
                Enter the row (1-8) of where you want to move the piece:
                Example: 4
                Enter ->
                """);
        int endRow = Integer.parseInt(scanner.nextLine().strip());
        System.out.println("""
                Enter the column (a-h) of where you want to move the piece:
                Example: a
                Enter ->
                """);
        char endColChar = scanner.nextLine().strip().toLowerCase().charAt(0);
        if (endRow < 1 || endRow > 8 || endColChar < 'a' || endColChar > 'h') {
            return "Invalid ending position! Please enter a valid row (1-8) and column (a-h)";
        }
        Integer endCol = charToNumMap.get(endColChar);
        ChessPosition endPosition = new ChessPosition(endRow, endCol);
        ChessPiece.PieceType promotion = null;
        ChessBoard board = game.getBoard();
        ChessPiece piece = board.getPiece(startPosition);
        if (isPromotionMove(endPosition, piece)) {
            System.out.println("""
                    You can Promote a Pawn! 
                    What would you like to promote it to: (Queen, Rook, Bishop, Knight)
                    Enter the piece ->
                    """);
            String promotionInput = scanner.nextLine().strip().toLowerCase();
            switch (promotionInput) {
                case "queen":
                    promotion = ChessPiece.PieceType.QUEEN;
                    break;
                case "rook":
                    promotion = ChessPiece.PieceType.ROOK;
                    break;
                case "bishop":
                    promotion = ChessPiece.PieceType.BISHOP;
                    break;
                case "knight":
                    promotion = ChessPiece.PieceType.KNIGHT;
                    break;
                default:
                    System.out.println("Invalid promotion piece! Please enter a valid piece (Queen, Rook, Bishop, Knight).");
                    return "Invalid promotion piece!";
            }
        }
        ChessMove move = new ChessMove(startPosition, endPosition, promotion);
        server.makeMove(new Make_Move(userAuth.authToken(), gameId, move));
        return "Successfully move";
    }

    public boolean isPromotionMove(ChessPosition toPosition, ChessPiece piece) {
        int endRow = toPosition.getRow();
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && (endRow == 1 || endRow == 8)) {
            return true;
        }
        return false;
    }

    private String leave() throws ResponseException {
        server.leave(new Leave(userAuth.authToken(), gameId));
        return "Exiting Game";
    }

    private String redraw() {
        return "";
    }

    private String help() {
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
