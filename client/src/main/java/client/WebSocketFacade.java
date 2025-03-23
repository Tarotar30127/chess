package client;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade {
    private Session session;
    public WebSocketFacade(String url) throws Exception {
        url = url.replace("http", "ws");
        URI socketURI = new URI(url + "/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, socketURI);
    }

    private void connect(String url) throws Exception {
        try {
            URI uri = new URI(url);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, uri);
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception("Connection failed: " + ex.getMessage());
        }
    }


}