package server;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public record Connection(Session session, int gameId) {

    public void send(String message) throws IOException {
        session.getRemote().sendString(message);
    }
}