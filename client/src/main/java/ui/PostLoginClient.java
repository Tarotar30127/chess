package ui;

import chess.ChessGame;
import client.ServerFacade;
import exception.ResponseException;
import model.AuthData;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostLoginClient {
    private final Scanner scanner = new Scanner(System.in);
    private ServerFacade server;
    AuthData userauth;

    public PostLoginClient(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
        this.userauth = null;
    }

    public String eval(String in, AuthData userAuth) throws ResponseException, IOException, URISyntaxException {
        this.userauth = userAuth;
        int number = Integer.parseInt(in.strip());
        return switch (number){
            case 1 -> help();
            case 2 -> logout();
            case 3 -> createGame();
            case 4 -> listGame();
            case 5 -> playGame();
            case 6 -> observerGame();
            case 7 -> quit();
            default -> "Invalid command";
        };
    }

    private String observerGame() throws ResponseException, IOException, URISyntaxException {
        System.out.println("Enter a Game ID>");
        int gameId = Integer.parseInt(scanner.nextLine());
        Object resp = null;
        resp = server.observeGame(gameId, this.userauth);
        return resp.toString();
    }

    private String playGame() throws ResponseException {
        String teamColor = null;
        System.out.println("Enter a Game ID>");
        int gameId = Integer.parseInt(scanner.nextLine());
        System.out.println("Type W for white player or B for black player>");
        String playerColor = scanner.nextLine();
        if (playerColor.toLowerCase().strip().equals("w")){
            teamColor = "WHITE";
        }
        if (playerColor.toLowerCase().strip().equals("b")){
            teamColor = "BLACK";
        }
        Object resp = server.playGame(teamColor, gameId, this.userauth);
        ChessGame game = new ChessGame();
        BoardPrintLayout.drawChessBoard(System.out, ChessGame.TeamColor.valueOf(teamColor), game);
        return resp.toString();
    }

    private String listGame() throws ResponseException {
        Object resp = server.listGame(this.userauth);
        return resp.toString();
    }

    private String createGame() throws ResponseException {
        System.out.println("Enter a Game Name>");
        String gameName = scanner.nextLine();
        Object resp = server.createGame(gameName, this.userauth);
        return resp.toString();
    }

    private String logout() throws ResponseException {
        Object resp = server.logout(userauth);
        return resp != null ? resp.toString() : "Logout successful";
    }

    private String quit() {
        return "quit";
    }

    private String help() {
        return """
                - Help : Displays text informing the user what actions they can take.
                - Quit : Exits the program.
                - Logout : Logs out the user
                - createGame : <Game Name>
                - listGame : Creates a list of games
                - playGame : <ID> [White|Black]
                - observe : <ID>
               """;
    }
}

