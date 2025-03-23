package ui;

import client.Notifications;
import client.WebSocketFacade;
import com.google.gson.Gson;
import exception.ResponseException;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PreLoginClient {
    private final Scanner scanner = new Scanner(System.in);
    private final WebSocketFacade ws;
    private final Gson gson = new Gson();

    public PreLoginClient(String serverUrl, Repl) {
        WebSocketFacade tmpWs = null;
        try {
            tmpWs = new WebSocketFacade(serverUrl);
        } catch (Exception e) {
            System.out.println("WebSocket connection failed: " + e.getMessage());
        }
        ws = tmpWs;
    }

    // Evaluate user input and perform actions accordingly
    public String eval(String in) {
        try {
            var tokens = in.split(" ");
            return switch (tokens) {
                case new String[]{"1"} -> help();
                case new String[]{"2"} -> quit();
                case new String[]{"3"} -> login();
                case new String[]{"4"} -> register();
                default -> "Invalid command";
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    // Collect registration details and send the register command via WebSocket
    private String register() {
        System.out.println("Enter a UserName>");
        String userName = scanner.nextLine();
        System.out.println("Enter a Password>");
        String password = scanner.nextLine();
        System.out.println("Enter an Email>");
        String email = scanner.nextLine();

        // Create a JSON command for registration
        Map<String, String> command = new HashMap<>();
        command.put("action", "register");
        command.put("username", userName);
        command.put("password", password);
        command.put("email", email);
        String jsonCommand = gson.toJson(command);

        // Send the command over the WebSocket
        if (ws != null) {
            ws.sendMessage(jsonCommand);
            return "Register command sent.";
        } else {
            return "WebSocket not connected.";
        }
    }

    private String login() {
        System.out.println("Enter Username>");
        String username = scanner.nextLine();
        System.out.println("Enter Password>");
        String password = scanner.nextLine();

        Map<String, String> command = new HashMap<>();
        command.put("action", "login");
        command.put("username", username);
        command.put("password", password);
        String jsonCommand = gson.toJson(command);

        if (ws != null) {
            ws.sendMessage(jsonCommand);
            return "Login command sent.";
        } else {
            return "WebSocket not connected.";
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
