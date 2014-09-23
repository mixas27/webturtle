package org.mixas.webturtle.net

/**
 * @author Mikhail Stryzhonok
 */
class SocketPoolTest extends GroovyTestCase {

    void testEmptyPool() {
        assertEquals(0, SocketPool.instance.currentSize)
    }

    void testDisconnectAndRemove() {
        def socket = new Socket();
        socket.bind(new InetSocketAddress(8080));
        SocketPool.instance.add(socket)

        SocketPool.instance.disconnectAndRemove(socket)
        assertEquals(0, SocketPool.instance.currentSize)
        assertTrue(socket.closed)
    }

    void testClearPool() {
        def socket1 = new Socket();
        socket1.bind(new InetSocketAddress(8080))
        def socket2 = new Socket();
        socket2.bind(new InetSocketAddress(8081))

        SocketPool.instance.add(socket1)
        SocketPool.instance.add(socket2)

        SocketPool.instance.clearPool()

        assertEquals(0, SocketPool.instance.currentSize)
        assertTrue(socket1.closed)
        assertTrue(socket2.closed)
    }
}
