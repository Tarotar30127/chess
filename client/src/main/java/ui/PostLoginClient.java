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
            default -> "Invalid command";
        };
    }

    private String observerGame() {
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
    }

    private String help() {
        return null;
    }
}

