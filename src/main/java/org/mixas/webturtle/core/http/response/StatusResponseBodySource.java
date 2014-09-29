package org.mixas.webturtle.core.http.response;

import org.mixas.webturtle.util.ResponseBodyUtils;

/**
 * @author Mikhail Stryzhonok
 */
public class StatusResponseBodySource implements ResponseBodySource {

    private HttpResponseStatus status;

    public StatusResponseBodySource(HttpResponseStatus status) {
        this.status = status;
    }

    @Override
    public String getResponseBody() {
        return ResponseBodyUtils.getStatusResourceResponseBody(status);
    }
}
