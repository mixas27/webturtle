package org.mixas.webturtle.core;

import javax.servlet.http.Cookie;
import java.util.Map;

/**
 * @author Mikhail Stryzhonok
 */
public class HttpRequest {

    public static final String COOKIE_HEADER = "Cookie";
    private static final String COOKIE_SEPARATOR = ";";
    private static final String COOKIE_PAIR_SEPARATOR = "=";

    private HttpRequestMethod method;
    private String protocolVersion;
    private Map<String, String>  headers;
    private Cookie[] cookies;
    private String resource;

    public HttpRequest(HttpRequestMethod method, String resource, String protocolVersion, Map<String, String> headers) {
        this.method = method;
        this.resource = resource;
        this.protocolVersion = protocolVersion;
        this.headers = headers;
        initCookies();
    }

    private void initCookies() {
        String cookieString  = headers.get(COOKIE_HEADER);
        if (cookieString != null && !cookieString.isEmpty()) {
            headers.remove(COOKIE_HEADER);
            String[] cookiePairs = cookieString.split(COOKIE_SEPARATOR);
            cookies = new Cookie[cookiePairs.length];
            for (int i = 0; i < cookiePairs.length; i++) {
                cookies[i] = fromString(cookiePairs[i]);

            }
        } else {
            cookies = new Cookie[0];
        }
    }

    private Cookie fromString(String cookiePair) {
        String[] nameAndValue = cookiePair.split(COOKIE_PAIR_SEPARATOR);
        if (nameAndValue.length == 2) {
            return new Cookie(nameAndValue[0].trim(), nameAndValue[1].trim());
        } else if (nameAndValue.length > 2) {
            StringBuffer value = new StringBuffer(nameAndValue[1]);
            for (int i = 2; i < nameAndValue.length; i ++) {
                value.append(COOKIE_PAIR_SEPARATOR);
                value.append(nameAndValue[i]);
            }
            return new Cookie(nameAndValue[0].trim(), value.toString().trim());
        } else {
            return null;
        }
    }

    public Cookie[] getCookies() {
        return cookies;
    }

}
