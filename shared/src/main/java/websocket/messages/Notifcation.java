package websocket.messages;

public class Notifcation extends ServerMessage{
    public Notifcation(String msg) {
        super(ServerMessageType.NOTIFICATION, msg);
    }
}
