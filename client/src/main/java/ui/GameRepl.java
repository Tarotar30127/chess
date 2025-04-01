package ui;

import exception.ResponseException;
import org.sparkproject.dmg.pmml.False;

import java.util.Scanner;

public class GameRepl {
    private boolean active;
    private final Scanner scanner;

    public GameRepl() {
        this.active = true;
        this.scanner = new Scanner(System.in);
    }
    public void deactivate() {
        this.active = false;
    }
    public void run(String gameID) throws ResponseException {
        System.out.printf("Welcome to the Game %s!%n", gameID);
        while (active) {
            System.out.println("""
            Commands:
            1 : Help
            2 : Redraw Chess Board
            3 : Leave
            4 : Make Move
            5 : Resign
            6 : Highlight Legal Moves
            Enter the number for the command:
            """);
            String command = scanner.nextLine();
            String com = GameClient.eval(command);
            if (com.contains("Exiting")){
                deactivate();
            }
            System.out.println(com);
        }
        System.out.println("Exiting Game...");
    }



}
