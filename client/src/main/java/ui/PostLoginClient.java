package ui;

public class PostLoginClient {
        private String visitorName = null;
        private final ServerFacade server;
        private final String serverUrl;
        private final NotificationHandler notificationHandler;
        private State state = State.SIGNEDOUT;

    public PostLoginClient(String serverUrl, Repl rep) {
            server = new ServerFacade(serverUrl);
            this.serverUrl = serverUrl;
            this.notificationHandler = notificationHandler;
        }

        public String eval(String in) {
            try {
                var tokens = in.toLowerCase().split(" ");
                var cmd = (tokens.length > 0) ? tokens[0] : "help";
                var params = Arrays.copyOfRange(tokens, 1, tokens.length);
                return switch (cmd) {
                    case "signin" -> signIn(params);
                    case "rescue" -> rescuePet(params);
                    case "list" -> listPets();
                    case "signout" -> signOut();
                    case "adopt" -> adoptPet(params);
                    case "adoptall" -> adoptAllPets();
                    case "quit" -> "quit";
                    default -> help();
                };
            } catch (ResponseException ex) {
                return ex.getMessage();
            }
        }
    }

