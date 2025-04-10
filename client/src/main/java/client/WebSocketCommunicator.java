package client;

import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

public class WebSocketCommunicator extends Endpoint {

    Session session;
    ServerMessageObserver notificationHandler;
    private Timer pingTimer;
    private static final long PING_INTERVAL = 30000;


    public WebSocketCommunicator(String url, ServerMessageObserver notificationHandler) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            startPingTask();

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    notificationHandler.notify(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            try {
                throw new ResponseException(500, ex.getMessage());
            } catch (ResponseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void startPingTask() {
        pingTimer = new Timer(true);
        pingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendPing();
            }
        }, PING_INTERVAL, PING_INTERVAL);
    }

    private void sendPing() {
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText("ping");
            }
        } catch (IOException e) {
            System.err.println("Error sending ping: " + e.getMessage());
        }
    }

    @Override
    public void onError(Session session, Throwable error) {
        System.err.println("WebSocket error:");
        error.printStackTrace();
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("=== WebSocket Connection Closed ===");
        System.out.println("Reason Code: " + closeReason.getCloseCode());
        System.out.println("Reason Phrase: " + closeReason.getReasonPhrase());
        if (pingTimer != null) {
            pingTimer.cancel();
        }

        if (closeReason.getReasonPhrase() != null &&
                closeReason.getReasonPhrase().contains("Idle timeout")) {
            System.out.println("Disconnected due to inactivity. Consider sending periodic pings.");
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }


    public void send(UserGameCommand command) throws ResponseException {
        try {
            String json = new Gson().toJson(command);
            session.getBasicRemote().sendText(json);
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}