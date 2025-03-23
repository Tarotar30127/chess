package ui;

import client.ServerFacade;
import exception.ResponseException;

import java.util.Scanner;

public class PostLoginClient {
    private final Scanner scanner = new Scanner(System.in);
    private ServerFacade server;

    public PostLoginClient(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
    }

    public String eval(String in) {
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

    private String observerGame() {
        return null;
    }

    private String playGame() {
        return null;
    }

    private String listGame() {
        return null;
    }

    private String createGame() {
        return null;
    }

    private String logout() {
        return null;
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

