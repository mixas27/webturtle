package org.mixas.webturtle;

import org.mixas.webturtle.configuration.Configurer;
import org.mixas.webturtle.configuration.MissingPropertyException;
import org.mixas.webturtle.configuration.XmlScenarioParser;
import org.mixas.webturtle.core.http.request.HttpRequest;
import org.mixas.webturtle.core.http.response.HttpResponse;
import org.mixas.webturtle.net.Server;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/**
 * Application entry point
 *
 * @author Mikhail Stryzhonok
 */
public class WebTurtle {

    public static final String STOP_WORD = "stop";

    public static void main(String[] args) {
        XmlScenarioParser parser = new XmlScenarioParser();
        Map<HttpRequest, HttpResponse> map = parser.parse("d:/projects/own/web-turtle/example/scenario.xml");
        System.out.println("Application configuration ...");
        System.out.println("Success !");
        System.out.println("Application startup ...");
        final Server server = new Server();
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
