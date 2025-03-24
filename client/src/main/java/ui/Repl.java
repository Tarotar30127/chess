package ui;

import model.AuthData;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Repl {
    public enum State {
        SIGNEDOUT,
        SIGNEDIN
    }
    private static State state = State.SIGNEDOUT;
    private final PreLoginClient preClient;
    private final PostLoginClient postClient;
    private static final String WHITE = "\u001B[15m";

    private final Scanner scanner = new Scanner(System.in);
    private String result = "";
    private AuthData userAuth;



    public Repl(String serverUrl) {
        preClient = new PreLoginClient(serverUrl);
        postClient = new PostLoginClient(serverUrl);
        this.userAuth = null;

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
            System.out.println(WHITE + result);
            if (result.startsWith("Login successful") || result.startsWith("Registration successful")) {
                state = State.SIGNEDIN;
                Pattern pattern = Pattern.compile("authToken=([^,]+), username=([^]]+)");
                Matcher matcher = pattern.matcher(result);
                if (matcher.find()) {
                    String authToken = matcher.group(1);
                    String username = matcher.group(2);
                    this.userAuth = new AuthData(authToken, username);
                }

            }
        } catch (Throwable e) {
            System.out.println(e.toString());
        }

    }

    private void postLogin() {
        String line = scanner.nextLine();
        try {
            result = postClient.eval(line, this.userAuth);
            System.out.println(WHITE + result);
            if (result.equalsIgnoreCase("Logout successful")) {
                state = State.SIGNEDOUT;
            }
        } catch (Throwable e) {
            System.out.println(e.toString());
        }
    }
}