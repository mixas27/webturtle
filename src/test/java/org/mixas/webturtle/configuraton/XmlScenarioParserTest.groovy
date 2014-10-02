package org.mixas.webturtle.configuraton

import org.mixas.webturtle.configuration.XmlScenarioParser
import org.mixas.webturtle.core.http.request.HttpRequest
import org.mixas.webturtle.core.http.request.HttpRequestMethod
import org.mixas.webturtle.core.http.response.HttpResponse
import org.mixas.webturtle.core.http.response.StringResponseBodySource

/**
 * @author Mikhail Stryzhonok
 */
class XmlScenarioParserTest extends GroovyTestCase {

    private def parser = new XmlScenarioParser(Thread.currentThread().contextClassLoader
            .getResource("org/mixas/webturtle/scheme/validSchema.xsd"));

    void testIsValidShouldThrowIllegalArgumentExceptionForNotExistingFile() {
        shouldFail(IllegalArgumentException) {
            parser.isValid("nonExisting")
        }
    }

    void testIsValidShouldThrowIllegalStateExceptionIfSchemaNotValid() {
        def parser = new XmlScenarioParser(Thread.currentThread().contextClassLoader
                .getResource("org/mixas/webturtle/scheme/notValidSchema.xsd"))
        shouldFail(IllegalStateException) {
            parser.isValid("anyxml")
        }
    }

    void testIsValidShouldReturnFalseIfXmlNotValid() {
        assertFalse(parser.isValid(Thread.currentThread().contextClassLoader
                .getResource("org/mixas/webturtle/xml/notValid.xml").path))
    }

    void testIsValidShouldReturnTrueIfXmlValid() {
        assertTrue(parser.isValid(Thread.currentThread().contextClassLoader
                .getResource("org/mixas/webturtle/xml/valid.xml").path))
    }

    void testParseShouldThrowIllegalArgumentExceptionForNotExistingFile() {
        shouldFail(IllegalArgumentException) {
            parser.parse("nonExisting")
        }
    }

    void testParse() {
        def result = parser.parse(Thread.currentThread().contextClassLoader
                .getResource("org/mixas/webturtle/xml/valid.xml").path)

        assertEquals(2, result.size())
        assertMap(result)
    }

    private void assertMap(Map<HttpRequest, HttpResponse> map) {
        for (e in map) {
            assertTrue(e.key.url.startsWith("/example"))
            assertEquals(HttpRequestMethod.GET, e.key.method)
            assertTrue(e.value.source instanceof StringResponseBodySource)
        }
    }
}
