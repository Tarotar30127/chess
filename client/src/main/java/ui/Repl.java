package ui;


import ui.PostLoginClient;
import ui.PreLoginClient;

import java.util.Scanner;

import static client.EscapeSequences.*;

public class Repl {

    public Repl(String serverUrl) {
        preClient = new PreLoginClient(serverUrl, this);
        postClient = new PostLoginClient(serverUrl, this);

    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to Chess. Sign in to start.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

}