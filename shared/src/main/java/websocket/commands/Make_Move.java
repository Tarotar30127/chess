package websocket.commands;

public class Make_Move extends UserGameCommand{
    public Make_Move(String authToken, Integer gameID) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
    }
}
