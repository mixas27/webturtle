package org.mixas.webturtle.core;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Stryzhonok
 */
public class HttpResponse {

    private String body;
    private final Map<String, String> headers = new HashMap<>();
    private Cookie[] cookies;
    private HttpResponseStatus status;

    public HttpResponse(HttpResponseStatus errorStatus) {
        if (errorStatus.getCode() < 400) {
            throw new IllegalArgumentException();
        }
        this.status = errorStatus;
    }
}
