package ui;

import chess.ChessGame;
import client.ServerFacade;
import com.google.gson.JsonObject;
import exception.ResponseException;
import model.AuthData;

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
    String serverURL;

    public PostLoginClient(String serverUrl) {
        this.serverURL = serverUrl;
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
        GameRepl gameRepl = new GameRepl(serverURL, gameId+1111, userauth, ChessGame.TeamColor.WHITE, true);
        gameRepl.run();
        return "success";
    }

    private String playGame() throws ResponseException {
        ChessGame.TeamColor teamColor = null;
        String strTeamColor = null;
        System.out.println("Enter a Game ID>");
        int gameId = parseInt(scanner.nextLine());
        int correctGameId = gameId + 1111;
        System.out.println("Type W for white player or B for black player>");
        String playerColor = scanner.nextLine();
        if (playerColor.toLowerCase().strip().equals("w")){
            teamColor = ChessGame.TeamColor.WHITE;
            strTeamColor = "WHITE";

        }
        else if (playerColor.toLowerCase().strip().equals("b")){
            teamColor = ChessGame.TeamColor.BLACK;
            strTeamColor = "BLACK";
        }
        else{
            return "Not a valid color";
        }
        Object resp = server.playGame(strTeamColor, correctGameId, this.userauth);
        if (resp.toString().contains("Error")) {
            return "Unsuccessfully";
        } else {
            GameRepl gameRepl = new GameRepl(serverURL, correctGameId, userauth, teamColor, false);
            gameRepl.run();

            return "Successfully";
        }
    }

    private String listGame() throws ResponseException {
        Map resp = server.listGame(this.userauth);
        List<Map<String, Object>> games = (List<Map<String, Object>>) resp.get("games");
        System.out.printf("  Game ID  |  Game Name  |  White User  |  Black User %n");
        for (Map<String, Object> gameData : games) {
            int gameID = ((Number) gameData.get("gameID")).intValue() - 1111;
            String gameName = (String) gameData.get("gameName");
            String whiteUsername = (String) gameData.getOrDefault("whiteUsername", "no user found");
            String blackUserName = (String) gameData.getOrDefault("blackUsername", "no user found");

            System.out.printf("  Game ID: %d | Game Name: %s  |  White User: %s  |  Black User: %s %n",
                    gameID, gameName, whiteUsername, blackUserName);
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

