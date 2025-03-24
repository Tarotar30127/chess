package ui;

import chess.ChessGame;
import client.ServerFacade;
import exception.ResponseException;
import model.AuthData;

import java.io.IOException;
import java.net.URISyntaxException;
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

    public String eval(String in, AuthData userAuth) throws ResponseException, IOException, URISyntaxException {
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

    private String observerGame() throws ResponseException, IOException, URISyntaxException {
        System.out.println("Enter a Game ID>");
        int gameId = parseInt(scanner.nextLine());
        Object resp = null;
        resp = server.observeGame(gameId, this.userauth);
        return resp.toString();
    }

    private String playGame() throws ResponseException {
        String teamColor = null;
        System.out.println("Enter a Game ID>");
        int gameId = parseInt(scanner.nextLine());
        int correctGameId = gameId+1111;
        System.out.println("Type W for white player or B for black player>");
        String playerColor = scanner.nextLine();
        if (playerColor.toLowerCase().strip().equals("w")){
            teamColor = "WHITE";
        }
        if (playerColor.toLowerCase().strip().equals("b")){
            teamColor = "BLACK";
        }
        Object resp = server.playGame(teamColor, correctGameId, this.userauth);
        ChessGame game = new ChessGame();
        BoardPrintLayout.drawChessBoard(System.out, ChessGame.TeamColor.valueOf(teamColor), game);
        return resp.toString();
    }

    private String listGame() throws ResponseException {
        Map resp = server.listGame(this.userauth);
        List<Map<String, Object>> games = (List<Map<String, Object>>) resp.get("games");
        StringBuilder out = new StringBuilder();
        System.out.printf("  Game ID  []  Game Name  []  White User  []  Black User %n");
        Pattern gamePattern = Pattern.compile("gameID=(\\d+).*?gameName=(\\w+)", Pattern.DOTALL);
        Pattern whitePlayerPattern = Pattern.compile("whiteUserName=(\\w*)");
        Pattern blackPlayerPattern = Pattern.compile("blackUserName=(\\w*)");
        for (Map<String, Object> gameData : games) {
            String gameDataStr = gameData.toString();
            Matcher gameMatcher = gamePattern.matcher(gameDataStr);
            if (gameMatcher.find()) {
                String gameIDStr = gameMatcher.group(1);
                int gameID = Integer.parseInt(gameIDStr) - 1111;
                String gameName = gameMatcher.group(2);
                Matcher whiteMatcher = whitePlayerPattern.matcher(gameDataStr);
                String whiteUsername = "no user found";
                if (whiteMatcher.find()) {
                    whiteUsername = whiteMatcher.group(1);
                }
                Matcher blackMatcher = blackPlayerPattern.matcher(gameDataStr);
                String blackUsername = "no user found";
                if (blackMatcher.find()) {
                    blackUsername = blackMatcher.group(1);
                }
                System.out.printf("  Game ID: %d [] Game Name: %s  []  White User: %s  []  Black User: %s %n",
                        gameID, gameName, whiteUsername, blackUsername);
            }
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

