package ui;

import client.ServerFacade;
import client.ServerMessageObserver;
import exception.ResponseException;
import model.AuthData;

import java.io.IOException;
import java.net.URISyntaxException;

import java.util.Scanner;

public class PreLoginClient {
    private final Scanner scanner = new Scanner(System.in);
    private ServerFacade server;

    public PreLoginClient(String serverUrl) throws ResponseException {
        this.server = new ServerFacade(serverUrl);

    }

    public String eval(String in) throws ResponseException {
        int number = Integer.parseInt(in.strip());
        return switch (number) {
            case 1 -> help();
            case 2 -> quit();
            case 3 -> login();
            case 4 -> register();
            default -> "Invalid command";
        };
    }

    private String register() throws ResponseException {
        System.out.println("Enter a UserName>");
        String userName = scanner.nextLine();
        if (userName == null) {
            return "invalid name";
        }
        System.out.println("Enter a Password>");
        String password = scanner.nextLine();
        if (password == null) {
            return "invalid password";
        }
        System.out.println("Enter an Email>");
        String email = scanner.nextLine();
        if (email == null) {
            return "invalid email";
        }
        AuthData resp = null;
        try {
            resp = server.register(userName, password, email);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
        return "Registration successful: " + resp;
    }

    private String login() {
        System.out.println("Enter Username>");
        String userName = scanner.nextLine();
        if (userName == null) {
            return "invalid name";
        }
        System.out.println("Enter Password>");
        String password = scanner.nextLine();
        if (password == null) {
            return "invalid password";
        }
        try {
            Object resp = server.login(userName, password);
            return "Login successful: " + resp;
        } catch (ResponseException e) {
            return "Login failed: " + e.getMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String quit() {
        return "quit";
    }

    private String help() {
        return """
                - Help : Displays text informing the user what actions they can take.
                - Quit : Exits the program.
                - Login : <Username> <Password>
                - Register : <Username> <Password> <Email>
                """;
    }

}
