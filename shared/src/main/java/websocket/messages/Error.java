package websocket.messages;



public class Error extends ServerMessage{
    public Error(String msg) {
        super(ServerMessageType.ERROR, msg);
    }
}
