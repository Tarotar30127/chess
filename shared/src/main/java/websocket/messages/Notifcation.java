package websocket.messages;


public class Notifcation extends ServerMessage{
    String message;
    public Notifcation(String msg) {
        super(ServerMessageType.NOTIFICATION);
        this.message = msg;
    }
    public String getMessage(){
        return this.message;
    }
}
