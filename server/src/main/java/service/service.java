package service;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.joinColorId;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class service {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public service(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public joinColorId joinGame(Integer gameID, String PlayerColor, String authToken) throws ResponseException {
        AuthData auth = authDAO.getAuth(authToken);
        if (auth != null) {
            GameData possibleGame;
            try {
                possibleGame = gameDAO.getGame(gameID);
            } catch (RuntimeException e) {
                throw new ResponseException(400, "Error: bad request");
            }
            String userName = auth.username();
            String WhitePlayer = possibleGame.whiteUserName();
            String BlackPlayer = possibleGame.blackUserName();
            if (PlayerColor==null){
                throw new ResponseException(400, "Error: bad request");
            }
            if (PlayerColor.equals("WHITE")) {
                if (WhitePlayer != null && !WhitePlayer.equals(userName)) {
                    return null;
                }
                WhitePlayer = userName;
            }
            if (PlayerColor.equals("BLACK")) {
                if (BlackPlayer != null && !BlackPlayer.equals(userName)) {
                    return null;
                }
                BlackPlayer = userName;
            }
            gameDAO.updatePlayers(new GameData(possibleGame.gameId(), WhitePlayer, BlackPlayer, possibleGame.gameName(), possibleGame.game()));
            return new joinColorId(PlayerColor, gameID);
        }
        else{
            throw new ResponseException(401, "Error: unauthorized");
        }

    }


    public AuthData loginUser(UserData user) throws ResponseException {
        UserData retrivedUser;
        try {
            retrivedUser = userDAO.getUser(user.username());
            if (retrivedUser != null) {
                if (!Objects.equals(retrivedUser.password(), user.password())) {
                    retrivedUser = null;
                }
            }
        } catch (ResponseException e) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        if (retrivedUser != null) {
            String AuthToken = generateToken();
            AuthData authData = new AuthData(AuthToken, user.username());
            authDAO.addAuth(authData);
            return authData;
        }
        throw new ResponseException(401, "Error: unauthorized");

    }

    public AuthData registerUser(UserData user) throws ResponseException {
        try {
            userDAO.createUser(user);
        } catch (ResponseException e) {
            throw new ResponseException(403, "Error: already taken");
        }
        String AuthToken = generateToken();
        AuthData authData = new AuthData(AuthToken, user.username());
        authDAO.addAuth(authData);
        return authData;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void logout(String authToken) throws ResponseException {
        if (authDAO.getAuth(authToken) != null) {
            try {
                authDAO.deleteAuth(authToken);
            } catch (RuntimeException e) {
                throw new ResponseException(500, "Error: unable to logout");
            }
        }
        else{
            throw new ResponseException(401, "Error: unauthorized");
        }

    }

    public int createGame(String gameName, String authToken) throws ResponseException {
        if (authDAO.getAuth(authToken) != null){
            if (gameName.isEmpty()){
                throw new ResponseException(400, "Error: bad request");
            }
            int gameid = gameDAO.createGame(gameName);
            return gameid;
        }
        else {
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    public Collection<GameData> getGame(String authToken) throws ResponseException {
        if (authDAO.getAuth(authToken) != null){
            return gameDAO.listgames();
        }
        else{
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    public void clear() throws ResponseException{
        try {
            gameDAO.clear();
            authDAO.clear();
            userDAO.clear();
        } catch (ResponseException e) {
            throw new ResponseException(500, "Error: unable to clear");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof service that)) {
            return false;
        }
        return Objects.equals(userDAO, that.userDAO) && Objects.equals(authDAO, that.authDAO) && Objects.equals(gameDAO, that.gameDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userDAO, authDAO, gameDAO);
    }
}