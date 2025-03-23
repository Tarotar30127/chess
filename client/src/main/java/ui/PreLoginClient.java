package ui;

import client.ServerFacade;
import com.google.gson.Gson;
import exception.ResponseException;

import java.util.Scanner;

public class PreLoginClient {
    private final Scanner scanner = new Scanner(System.in);
    private final Gson gson = new Gson();
    private ServerFacade server;

    public PreLoginClient(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
    }

    public String eval(String in) {
        int number = Integer.parseInt(in.strip());
        return switch (number) {
            case 1 -> help();
            case 2 -> quit();
            case 3 -> login();
            case 4 -> register();
            default -> "Invalid command";
        };
    }

    private String register() {
        System.out.println("Enter a UserName>");
        String userName = scanner.nextLine();
        System.out.println("Enter a Password>");
        String password = scanner.nextLine();
        System.out.println("Enter an Email>");
        String email = scanner.nextLine();
        try {
            Object resp = server.register(userName, password, email);
            return "Registration successful. Auth token: " + resp;
        } catch (ResponseException e) {
            return "Registration failed: " + e.getMessage();
        }
    }

    private String login() {
        System.out.println("Enter Username>");
        String userName = scanner.nextLine();
        System.out.println("Enter Password>");
        String password = scanner.nextLine();
        try {
            Object resp = server.login(userName, password);
            return "Login successful: " + resp;
        } catch (ResponseException e) {
            return "Login failed: " + e.getMessage();
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
