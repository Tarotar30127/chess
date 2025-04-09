package client;


import websocket.messages.ServerMessage;

public interface ServerMessageObserver {
    void notify(String notification);
}

