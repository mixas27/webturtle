package org.mixas.webturtle.net;

import org.apache.log4j.Logger;
import org.mixas.webturtle.core.http.ResponseMapping;
import org.mixas.webturtle.core.http.request.HttpRequest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * @author Mikhail Stryzhonok
 */
public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class);

    private static final int DEFAULT_PORT = 8080;
    private int port;
    private ServerSocket serverSocket;
    private final ResponseMapping responseMapping;

    public Server(int port, ResponseMapping responseMapping) {
        this.port = port;
        this.responseMapping = responseMapping;
    }

    public Server(ResponseMapping responseMapping) {
        this(DEFAULT_PORT, responseMapping);
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
        LOGGER.debug("Srever started on port " + port);
        do {
            final Socket aceptedSocket = serverSocket.accept();
            LOGGER.debug("Accepted connection from " + aceptedSocket.getInetAddress().getHostAddress()
                    + ":" + aceptedSocket.getPort());
            SocketPool.getInstance().add(aceptedSocket);
            Thread handlerThread = new Thread(new ConnectionHandler(aceptedSocket, responseMapping));
            handlerThread.start();
        } while (!serverSocket.isClosed());
    }

    public void stop() throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
        SocketPool.getInstance().clearPool();
        LOGGER.debug("Server stopped");
    }

    public void printRequestStatistic() {
        Map<HttpRequest, Integer> stats = responseMapping.getAllStatistic();
        for (Map.Entry<HttpRequest, Integer> entry : stats.entrySet()) {
            System.out.println("Url " + entry.getKey().getUrl() + " accessed " + entry.getValue()
                    + " times with method " + entry.getKey().getMethod());
        }
    }

    public boolean isStopped() {
        return serverSocket.isClosed();
    }

    public void setPort(int port) {
        this.port = port;
    }
}
