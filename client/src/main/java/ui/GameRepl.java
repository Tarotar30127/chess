package ui;

import chess.ChessGame;
import client.ServerFacade;
import client.ServerMessageObserver;
import exception.ResponseException;

import java.util.Scanner;

import model.AuthData;
import websocket.commands.Connect;



public class GameRepl implements ServerMessageObserver {
    private boolean active;
    private final Scanner scanner;
    private GameClient client;
    private boolean observer = false;
    AuthData userAuth;
    int gameId;
    private ServerFacade server;
    public ChessGame.TeamColor color;



    public GameRepl(String serverUrl, int gameID, AuthData userAuth, ChessGame.TeamColor color, boolean observe) throws ResponseException {
        this.gameId = gameID;
        this.userAuth = userAuth;
        this.color = color;
        this.active = true;
        this.observer = observe;
        this.scanner = new Scanner(System.in);
        client = new GameClient(serverUrl, userAuth, this.gameId, color);
    }
    public void deactivate() {
        this.active = false;
    }

    public void run() throws ResponseException {
        if (observer == false){
            server.joinPlayer(new Connect(userAuth.authToken(), gameId, color), this);
        }else {
            server.joinObserver(new Connect(userAuth.authToken(), gameId, null), this);
        }
        System.out.printf("Welcome to the Game %s!%n", this.gameId);
        while (active) {
            System.out.println("""
            Commands:
            1 : Help
            2 : Redraw Chess Board
            3 : Leave
            4 : Make Move
            5 : Resign
            6 : Highlight Legal Moves
            7 : Reset Board
            Enter the number for the command:""");
            System.out.println("Game->");
            String command = scanner.nextLine();
            String com = client.eval(command, this.observer);
            if (com.contains("Exiting")){
                deactivate();
            }
            System.out.println(com);
        }
        System.out.println("Exiting Game...");
    }

}
