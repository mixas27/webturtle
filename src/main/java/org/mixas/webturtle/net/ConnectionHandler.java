package org.mixas.webturtle.net;

import java.net.Socket;

/**
 * @author Mikhail Stryzhonok
 */
public class ConnectionHandler implements Runnable {

    private final Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("i am connection handler");
    }
}
