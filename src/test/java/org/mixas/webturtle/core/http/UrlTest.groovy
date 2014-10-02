package org.mixas.webturtle.core.http

/**
 * @author Mikhail Stryzhonok
 */
class UrlTest extends GroovyTestCase {

    void testCreateUrlWithoutSlashAtTheEnd() {
        def value = "example"
        def url = new Url(value)

        assertEquals(value + "/", url.value)
    }

    void testCreateUrlWithSlashAtTheEnd() {
        def value = "example/"
        def url = new Url(value)

        assertEquals(value, url.value)
    }

    void testCreateUrlWithMultipleSlashes() {
        def url = new Url("example///")

        assertEquals("example/", url.getValue())
    }
}
