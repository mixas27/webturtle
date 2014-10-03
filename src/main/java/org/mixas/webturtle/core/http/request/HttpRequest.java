package org.mixas.webturtle.core.http.request;

import org.mixas.webturtle.core.http.Url;

import javax.servlet.http.Cookie;
import java.util.Collections;
import java.util.Map;

/**
 * @author Mikhail Stryzhonok
 */
public class HttpRequest {
    private static final String DEFAULT_PROTOCOL_VERSION = "HTTP/1.1";
    public static final String COOKIE_HEADER = "Cookie";
    private static final String COOKIE_SEPARATOR = ";";
    private static final String COOKIE_PAIR_SEPARATOR = "=";

    private HttpRequestMethod method;
    private String protocolVersion;
    private Map<String, String>  headers;
    private Cookie[] cookies;
    private Url url;

    public HttpRequest() {
        protocolVersion = DEFAULT_PROTOCOL_VERSION;
        headers = Collections.emptyMap();
        cookies = new Cookie[0];
    }

    public HttpRequest(HttpRequestMethod method, String url, String protocolVersion, Map<String, String> headers) {
        this.method = method;
        this.url = new Url(url);
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

    public String getProtocolVersion() {
        return protocolVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpRequest that = (HttpRequest) o;

        if (method != that.method) return false;
        if (!url.equals(that.url)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = method.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }

    public void setMethod(HttpRequestMethod method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = new Url(url);
    }

    /**
     * Needed for tests
     * @return the request method
     */
    public HttpRequestMethod getMethod() {
        return method;
    }

    /**
     * Needed for tests
     * @return the request URl
     */
    public String getUrl() {
        return url.getValue();
    }
}
