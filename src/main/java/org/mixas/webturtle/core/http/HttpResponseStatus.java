package org.mixas.webturtle.core.http;

/**
 * Now it is most common used response codes. Will be supplemented in future if will necessary.
 *
 * @author Mikhail Stryzhonok
 */
public enum HttpResponseStatus {
    OK (200),
    MULTIPLE_CHOICES (300),
    MOVED_PERMANENTLY (301),
    FOUND (302),
    NOT_MODIFIED (304),
    TEMPORARY_REDIRECT (307),
    BAD_REQUEST (400),
    UNAUTHORIZED (401),
    FORBIDDEN (403),
    NOT_FOUND (404),
    GONE (410),
    INTERNAL_SERVER_ERROR (500),
    NOT_IMPLEMENTED (501),
    SERVICE_UNAVAILABLE (503),
    PERMISSION_DENIED (550);

    private final int code;

    HttpResponseStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public boolean isError() {
        return code < 400;
    }
}
