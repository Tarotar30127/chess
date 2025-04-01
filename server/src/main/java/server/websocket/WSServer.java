package server.websocket;

import org.apache.hadoop.shaded.org.eclipse.jetty.websocket.api.Session;
import org.apache.hadoop.shaded.org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.apache.hadoop.shaded.org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Spark;

@WebSocket
class WSServer {


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        session.getRemote().sendString("WebSocket response: " + message);
    }
}
