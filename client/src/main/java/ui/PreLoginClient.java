package ui;

import exception.ResponseException;

import java.util.Arrays;


public class PreLoginClient {



    public String eval(String in) {
        try {
            var tokens = in.split(" ");
            return switch (tokens) {
                case "1" -> help();
                case "2" -> quit();
                case "3" -> listPets();
                case "4" -> signOut();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
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
