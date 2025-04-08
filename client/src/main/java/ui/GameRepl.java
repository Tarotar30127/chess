package ui;

import chess.ChessGame;
import client.ServerMessageObserver;
import com.google.gson.Gson;
import exception.ResponseException;

import java.util.Scanner;
import websocket.messages.Error;
import websocket.messages.LoadGame;
import websocket.messages.Notifcation;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;


public class GameRepl implements ServerMessageObserver {
    private boolean active;
    private final Scanner scanner;
    private GameClient client;

    public GameRepl(String serverUrl) throws ResponseException {
        this.active = true;
        this.scanner = new Scanner(System.in);
        client = new GameClient(serverUrl, this);
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


    @Override
    public void notify(String notification) {
        if (notification.contains("\"serverMessageType\":\"NOTIFICATION\"")) {
            Notifcation notify = new Gson().fromJson(notification, Notifcation.class);
            printNotification(notify.getMessage());
        }
        else if (notification.contains("\"serverMessageType\":\"ERROR\"")) {
            Error error = new Gson().fromJson(notification, Error.class);
            printError(error.getMessage());
        }
        else if (notification.contains("\"serverMessageType\":\"LOAD_GAME\"")) {
            LoadGame loadGame = new Gson().fromJson(notification, LoadGame.class);
            printGame(loadGame.returnGame());
        }
    }

    private void printGame(ChessGame chessGame) {
        BoardPrintLayout.drawChessBoard(System.out, chessGame.getTeamTurn(), chessGame);
    }

    private void printError(String message) {
        System.out.println(SET_TEXT_COLOR_RED + "Error: " + message + RESET_TEXT_COLOR);
    }

    private void printNotification(String message) {
        System.out.println(SET_TEXT_COLOR_BLUE + "Notification: " + message + RESET_TEXT_COLOR);
    }


}
