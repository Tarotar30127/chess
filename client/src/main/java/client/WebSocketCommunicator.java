package client;

import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketCommunicator extends Endpoint {

    Session session;
    ServerMessageObserver notificationHandler;


    public WebSocketCommunicator(String url, ServerMessageObserver notificationHandler) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

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