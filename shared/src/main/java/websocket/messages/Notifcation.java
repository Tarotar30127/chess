package websocket.messages;


public class Notifcation extends ServerMessage{
    String message;
    public Notifcation(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
}
