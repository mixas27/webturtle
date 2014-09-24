package org.mixas.webturtle.core.http.response;

/**
 * @author Mikhail Stryzhonok
 */
public class StringResponseBodySource implements ResponseBodySource {

    private String body;

    public StringResponseBodySource(String body) {
        this.body = body;
    }

    @Override
    public String getResponseBody() {
        return body;
    }
}
