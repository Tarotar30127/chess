package ui;

import chess.ChessGame;
import client.ServerMessageObserver;
import com.google.gson.Gson;
import exception.ResponseException;

import java.util.Scanner;

import model.AuthData;
import model.GameData;
import websocket.messages.Error;
import websocket.messages.LoadGame;
import websocket.messages.Notifcation;

import static ui.EscapeSequences.*;


public class GameRepl implements ServerMessageObserver {
    private boolean active;
    private final Scanner scanner;
    private GameClient client;
    AuthData userAuth;
    int gameId;
    public ChessGame.TeamColor color;
    GameData game;


    public GameRepl(String serverUrl, int gameID, AuthData userAuth, ChessGame.TeamColor color, GameData currentGame) throws ResponseException {
        this.game = currentGame;
        this.gameId = gameID;
        this.userAuth = userAuth;
        this.color = color;
        this.active = true;
        this.scanner = new Scanner(System.in);
        client = new GameClient(serverUrl, this, userAuth, gameId, color , game);
    }
    public void deactivate() {
        this.active = false;
    }

    public void run() throws ResponseException {
        System.out.printf("Welcome to the Game %s!%n", gameId);
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
            String com = client.eval(command);
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
