package client;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.BoardPrintLayout;
import ui.EscapeSequences;
import websocket.messages.LoadGame;
import websocket.messages.Notifcation;

public interface ServerMessageObserver {
    default void notify(String notification){
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
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Error: " + message + EscapeSequences.RESET_TEXT_COLOR);
    }

    private void printNotification(String message) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "Notification: " + message + EscapeSequences.RESET_TEXT_COLOR);
    }
}

