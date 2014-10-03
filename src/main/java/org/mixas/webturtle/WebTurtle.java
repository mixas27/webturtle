package org.mixas.webturtle;

import org.mixas.webturtle.configuration.Configurer;
import org.mixas.webturtle.configuration.MissingPropertyException;
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
    public static final String RESTART_KEYWORD = "restart";

    private static Server server;

    public static void main(String[] args) {
        startServer();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (STOP_WORD.equalsIgnoreCase(input)) {
                stopServer();
                break;
            } else if (RESTART_KEYWORD.equalsIgnoreCase(input)) {
                restartServer();
            }
        }
    }

    public static void restartServer() {
        System.out.println("Restarting application...");
        stopServer();
        startServer();
    }

    public static void startServer() {
        System.out.println("Application startup ...");
        try {
            server = new Server(Configurer.getInstance().getResponseMapping());
        } catch (MissingPropertyException e) {
            throw new IllegalStateException("Xml not valid");
        }
        try {
            server.setPort(Configurer.getInstance().getIntProperty("port"));
        } catch (MissingPropertyException e) {
            System.out.println("Property with name \"port\" can't be found. Default port will be used");
        }
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
    }

    public static void stopServer() {
        System.out.println("Stopping application...");
        try {
            server.stop();
            server.printRequestStatistic();
        } catch (IOException e) {
            System.out.println("Error while stopping server!");
        }
    }
}
