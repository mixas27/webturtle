package org.mixas.webturtle.net;

import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mikhail Stryzhonok
 */
public class SocketPool {
    private final Set<Socket> pool = new HashSet<>();
    private static SocketPool INSTANCE = new SocketPool();

    public static SocketPool getInstance() {
        return INSTANCE;
    }

    private SocketPool() {
    }

    /**
     * Adds socket to pool
     * @param socket socket to be added
     */
    public synchronized void add(Socket socket) {
        pool.add(socket);
    }

    /**
     * Disconnects socket and removes from pool
     * @param socket - socket to be removed
     */
    public synchronized void disconnectAndRemove(Socket socket) {
        closeWithExceptionHandling(socket);
        pool.remove(socket);
    }

    public int getCurrentSize() {
        return pool.size();
    }

    /**
     * Disconnects all sockets and removes from pool
     */
    public synchronized void clearPool() {
        for (Socket socket : pool) {
            closeWithExceptionHandling(socket);
        }
        pool.clear();
    }

    private void closeWithExceptionHandling(Socket socket) {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {

        }
    }
}
