package org.mixas.webturtle.core.http

import org.mixas.webturtle.core.http.request.HttpRequest
import org.mixas.webturtle.core.http.request.HttpRequestMethod

/**
 * @author Mikhail Stryzhonok
 */
class HttpRequestTest extends GroovyTestCase {

    void testCookieInitialization() {
        def headers = [
                'Connection':'Keep-Alive',
                'Cookie':'GMT=-180; Sample=AAA=aa'
        ]
        def request = new HttpRequest(HttpRequestMethod.GET, "/", "HTTP/1.1", headers)

        assertEquals(2, request.cookies.length)
        assertEquals("GMT", request.cookies[0].name)
        assertEquals("-180", request.cookies[0].value)
        assertEquals("Sample", request.cookies[1].name)
        assertEquals("AAA=aa", request.cookies[1].value)
        assertNull(request.headers.get(HttpRequest.COOKIE_HEADER))
    }

    void testCookiesEmptyIfNoCookieHeader() {
        def headers = [
                'Connection' : 'Keep-alive'
        ]
        def request = new HttpRequest(HttpRequestMethod.GET, "/", "HTTP/1.1", headers)

        assertNotNull(request.cookies)
        assertEquals(0, request.cookies.length)
    }
}
