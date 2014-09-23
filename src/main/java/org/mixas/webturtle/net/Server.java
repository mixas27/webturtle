package org.mixas.webturtle.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Mikhail Stryzhonok
 */
public class Server {
    private static final int DEFAULT_PORT = 8080;
    private int port;
    private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
    }

    public Server() {
        port = DEFAULT_PORT;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));

        do {
            final Socket aceptedSocket = serverSocket.accept();
            SocketPool.getInstance().add(aceptedSocket);
            Thread handlerThread = new Thread(new ConnectionHandler(aceptedSocket));
            handlerThread.start();
        } while (!serverSocket.isClosed());
    }

    public void stop() throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
        SocketPool.getInstance().clearPool();
    }

    public void setPort(int port) {
        this.port = port;
    }
}
