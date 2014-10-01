package org.mixas.webturtle.core.http;

import org.mixas.webturtle.core.http.request.HttpRequest;
import org.mixas.webturtle.core.http.response.HttpResponse;

import java.util.Map;

/**
 * @author Mikhail Stryzhonok
 */
public class ResponseMapping {
    private final Map<HttpRequest, HttpResponse> mapping;

    public ResponseMapping(Map<HttpRequest, HttpResponse> mapping) {
        this.mapping = mapping;
    }

    public HttpResponse getResponse(HttpRequest request) {
        return mapping.get(request);
    }

}
