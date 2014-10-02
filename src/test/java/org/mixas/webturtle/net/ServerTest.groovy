package org.mixas.webturtle.net

import org.mixas.webturtle.core.http.ResponseMapping
/**
 * @author Mikhail Stryzhonok
 */
class ServerTest extends GroovyTestCase {

    void testStopServerShouldCloseServerSocket() {
        def server = new Server(new ResponseMapping(Collections.emptyMap()));
        server.serverSocket = new ServerSocket();
        server.serverSocket.bind(new InetSocketAddress(8080));
        
        server.stop();
        
        assertTrue(server.stopped)
    }
}
