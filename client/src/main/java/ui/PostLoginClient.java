package ui;

import chess.ChessGame;
import client.ServerFacade;
import exception.ResponseException;
import model.AuthData;
import model.GamesList;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class PostLoginClient {
    private final Scanner scanner = new Scanner(System.in);
    private ServerFacade server;
    AuthData userauth;

    public PostLoginClient(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
        this.userauth = null;
    }

    public String eval(String in, AuthData userAuth) throws ResponseException {
        this.userauth = userAuth;
        int number = parseInt(in.strip());
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

    private String observerGame() throws ResponseException {
        System.out.println("Enter a Game ID>");
        int gameId = parseInt(scanner.nextLine());
        Map resp = server.observeGame(gameId, this.userauth);
        if (resp.containsKey("Error")) {
            return "Game doesn't exist";
        }
        ChessGame game = new ChessGame();
        BoardPrintLayout.drawChessBoard(System.out, ChessGame.TeamColor.WHITE, game);
        return "success";
    }

    private String playGame() throws ResponseException {
        String teamColor = null;
        System.out.println("Enter a Game ID>");
        int gameId = parseInt(scanner.nextLine());
        int correctGameId = gameId + 1111;
        System.out.println("Type W for white player or B for black player>");
        String playerColor = scanner.nextLine();
        if (playerColor.toLowerCase().strip().equals("w")){
            teamColor = "WHITE";
        }
        else if (playerColor.toLowerCase().strip().equals("b")){
            teamColor = "BLACK";
        }
        else{
            return "Not a valid color";
        }
        Object resp = server.playGame(teamColor, correctGameId, this.userauth);
        ChessGame game = new ChessGame();
        BoardPrintLayout.drawChessBoard(System.out, ChessGame.TeamColor.valueOf(teamColor), game);

        return "Successfully";
    }

    private String listGame() throws ResponseException {
        Map resp = server.listGame(this.userauth);
        List<GamesList> games = (List<GamesList>) resp.get("games");
        System.out.printf("  Game ID  |  Game Name  |  White User  |  Black User %n");
        for (GamesList game : games) {
            // Adjust gameID if needed
            int gameID = game.gameID() - 1111;
            System.out.printf("  Game ID: %d | Game Name: %s  |  White User: %s  |  Black User: %s %n",
                    gameID, game.gameName(), game.whiteUsername(), game.blackUsername());
        }
        return "Successful printing list";
    }




    private String createGame() throws ResponseException {
        System.out.println("Enter a Game Name>");
        String gameName = scanner.nextLine();
        Object resp = server.createGame(gameName, this.userauth);
        String result = "";
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(resp.toString());
        if (matcher.find()) {
            result += "GameID : ";
            int gameID = Integer.parseInt(matcher.group()) - 1111;
            result += Integer.toString(gameID);
        }
        else{
            result += "Game Creation Failed";
        }
        return result;
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

