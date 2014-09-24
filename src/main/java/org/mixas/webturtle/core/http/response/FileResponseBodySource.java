package org.mixas.webturtle.core.http.response;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            String str;
            while ((str = reader.readLine()) != null) {
                buffer.append(str);
            }
        } catch (IOException e) {
        }
        return buffer.toString();
    }
}
