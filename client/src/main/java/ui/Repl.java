package ui;

import client.Notifications;
import client.WebSocketFacade;

import java.util.Scanner;

public class Repl implements Notifications {
    private final String serverUrl;

    public enum State {
        SIGNEDOUT,
        SIGNEDIN
    }
    private static State state = State.SIGNEDOUT;
    private final PreLoginClient preClient;
    private final PostLoginClient postClient;
    private static final String BLUE = "\u001B[34m";

    private final Scanner scanner = new Scanner(System.in);
    private String result = "";

    public Repl(String serverUrl) {
        preClient = new PreLoginClient(serverUrl);
        postClient = new PostLoginClient(serverUrl);
    }

    @Override
    public void notify(Object notification) {
        System.out.println("Notification: " + notification);
    }

    public void run() {
        System.out.println("Welcome to Chess!\n");
        while (!result.equals("quit")) {
            printPrompt();
            try {
                if (state == State.SIGNEDIN) {
                    postLogin();
                }
                else {
                    preLogin();
                }
            } catch (Throwable e) {
                System.out.println(e.toString());
            }
        }
        System.out.println("Exiting...");
    }

    private void printPrompt() {
        if (state == State.SIGNEDOUT) {
            System.out.println("""
            Commands:
            1 : Help
            2 : Quit
            3 : Login
            4 : Register
            Enter the number for the command:
            """);
            System.out.print("chess> ");
        } else {
            System.out.println("""
            Commands:
            1 : Help
            2 : Logout
            3 : Create Game
            4 : List Games
            5 : Play Game
            6 : Observe Game
            Enter the number for the command:
            """);
            System.out.print("Menu> ");
        }
    }

    private void preLogin() {
        String line = scanner.nextLine();
        try {
            result = preClient.eval(line);
            System.out.println(BLUE + result);
        } catch (Throwable e) {
            System.out.println(e.toString());
        }

    }
    private void postLogin() {
        String line = scanner.nextLine();
        try {
            result = postClient.eval(line);
            System.out.println(BLUE + result);
        } catch (Throwable e) {
            System.out.println(e.toString());
        }
    }
}