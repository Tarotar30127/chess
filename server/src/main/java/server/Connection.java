package server;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public final org.eclipse.jetty.websocket.api.Session session;
    public final int gameId;

    public Connection(Session session, int gameId) {
        this.session = session;
        this.gameId = gameId;
    }

    public void send(String message) throws IOException {
        session.getRemote().sendString(message);
    }
}