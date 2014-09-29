package org.mixas.webturtle.core.http.response;

import org.mixas.webturtle.util.ResponseBodyUtils;

/**
 * @author Mikhail Stryzhonok
 */
public class FileResponseBodySource implements ResponseBodySource {

    private String filePath;

    public FileResponseBodySource(String pathToResponse) {
        this.filePath = pathToResponse;
    }

    @Override
    public String getResponseBody() {
        return ResponseBodyUtils.getFileResponseBody(filePath);
    }
}
