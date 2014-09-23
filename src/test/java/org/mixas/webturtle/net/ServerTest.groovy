package org.mixas.webturtle.net

/**
 * @author Mikhail Stryzhonok
 */
class ServerTest extends GroovyTestCase {

    void testStopServerShouldCloseServerSocket() {
        def server = new Server();
        server.serverSocket = new ServerSocket();
        server.serverSocket.bind(new InetSocketAddress(8080));
        
        server.stop();
        
        assertTrue(server.stopped)
    }
}
