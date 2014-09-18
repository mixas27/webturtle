package org.mixas.webturtle;

import org.mixas.webturtle.net.Server;

import java.io.IOException;
import java.util.Scanner;

/**
 * Application entry point
 *
 * @author Mikhail Stryzhonok
 */
public class WebTurtle {

    public static final String STOP_WORD = "stop";

    public static void main(String[] args) {
        System.out.println("Application startup...");
        final Server server = new Server();
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.start();
                } catch (IOException e) {
                    System.out.println("server shut down");
                }
            }
        });
        serverThread.start();
        System.out.println("Done!");
        System.out.println("Type " + STOP_WORD + " to shutdown server");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (STOP_WORD.equalsIgnoreCase(input)) {
                try {
                    server.stop();
                } catch (IOException e) {
                    System.out.println("Error while stopping server!");
                }
                break;
            }
        }
    }
}
