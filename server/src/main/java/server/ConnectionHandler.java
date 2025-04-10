package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.Notifcation;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionHandler {
    private static final ConcurrentHashMap<Session, Connection> CONNECTIONS = new ConcurrentHashMap<>();

    public void add(Session session, int gameId,String serveMessage) throws IOException {
        var connection = new Connection(session, gameId);
        CONNECTIONS.put(session, connection);
        Notifcation notifcation = new Notifcation(serveMessage);
        broadcast(notifcation, gameId, session);
    }

    public void remove(Session session) {
        CONNECTIONS.remove(session);
    }

    public static void broadcast(ServerMessage notification, int gameId, Session senderSession) throws IOException {
        String jsonNotification = new Gson().toJson(notification);

        for (var entry : CONNECTIONS.entrySet()) {
            Session session = entry.getKey();
            Connection conn = entry.getValue();

            if (session.isOpen() && conn.gameId() == gameId) {
                if (!session.equals(senderSession)) {
                    session.getRemote().sendString(jsonNotification);
                }
            }
        }
    }

    public static void direct(ServerMessage message, Session session) throws IOException {
        if (session != null && session.isOpen()) {
            String jsonMessage = new Gson().toJson(message);
            session.getRemote().sendString(jsonMessage);
        }
    }
}
