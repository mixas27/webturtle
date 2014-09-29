package org.mixas.webturtle.util;

import org.apache.log4j.Logger;
import org.mixas.webturtle.core.http.response.HttpResponseStatus;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Mikhail Stryzhonok
 */
public class ResponseBodyUtils {
    private static final Logger LOGGER = Logger.getLogger(ResponseBodyUtils.class);
    private static final String CODE_TEMPLATE_REGEXP = "\\$\\{code\\}";
    private static final String STATUS_TEMPLATE_REGEXP = "\\$\\{status\\}";
    private static final String HTML_TEMPLATE_PATH = "org/mixas/webturtle/html/responseTemplate.html";

    public static String getStatusResourceResponseBody(HttpResponseStatus status) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(ResponseBodyUtils.class.getClassLoader()
                .getResourceAsStream(HTML_TEMPLATE_PATH)));
        StringBuffer buffer = new StringBuffer();
        String str;
        try {
            while ((str = reader.readLine()) != null) {
                buffer.append(str);
            }
        } catch (IOException e) {
            LOGGER.error("Error reading internal resource", e);
        }
        return buffer.toString().replaceAll(CODE_TEMPLATE_REGEXP, String.valueOf(status.getCode()))
                .replaceAll(STATUS_TEMPLATE_REGEXP, status.toString());
    }

    public static String getFileResponseBody(String path) {
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String str;
            while ((str = reader.readLine()) != null) {
                buffer.append(str);
            }
        } catch (IOException e) {
            LOGGER.error("Error reading external file", e);
        }
        return buffer.toString();
    }
}
