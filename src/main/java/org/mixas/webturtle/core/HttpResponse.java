package org.mixas.webturtle.core;

import javax.servlet.http.Cookie;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Stryzhonok
 */
public class HttpResponse {

    private String body;
    private final Map<String, String> generalHeaders = new HashMap<>();
    private final Map<String, String> responseHeaders = new HashMap<>();
    private Cookie[] newCookies;
    private HttpResponseStatus status;
    private String protocol;

    public HttpResponse(String protocol, HttpResponseStatus errorStatus) {
        if (!errorStatus.isError()) {
            throw new IllegalArgumentException();
        }
        this.status = errorStatus;
        initGeneralHeaders();
        newCookies = new Cookie[0];
    }

    private void initGeneralHeaders() {
        if (status.isError()) {
            generalHeaders.put("Connection", "close");
        } else {
            generalHeaders.put("Connection", "keep-alive");
        }
        generalHeaders.put("Server", "WebTurtle");
        generalHeaders.put("Date", new Date().toGMTString());
    }

    public String getSendableForm() {
        StringBuffer sendable = new StringBuffer(protocol)
                .append(" ")
                .append(status.getCode())
                .append(" ")
                .append(status)
                .append("\n");
        for (Map.Entry<String, String> generalHeader : generalHeaders.entrySet()) {
            sendable.append(generalHeader.getKey()).append(": ").append(generalHeader.getValue()).append("\n");
        }

        for (Map.Entry<String, String> responseHeader : responseHeaders.entrySet()) {
            sendable.append(responseHeader.getKey()).append(": ").append(responseHeader.getValue()).append("\n");
        }
        //TODO: Add cookie setting after implmenting custom ones

        sendable.append("\n");
        sendable.append(body);

        return sendable.toString();
    }

}
