package org.mixas.webturtle.util;

import org.apache.log4j.Logger;
import org.mixas.webturtle.core.http.HttpResponseStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Mikhail Stryzhonok
 */
public class ResponseBodyFactory {
    private static final Logger LOGGER = Logger.getLogger(ResponseBodyFactory.class);
    private static final String CODE_TEMPLATE_REGEXP = "\\$\\{code\\}";
    private static final String STATUS_TEMPLATE_REGEXP = "\\$\\{status\\}";
    private static final String HTML_TEMPLATE_PATH = "org/mixas/webturtle/html/responseTemplate.html";

    public static String getInternalResourceResponseBody(HttpResponseStatus status) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(ResponseBodyFactory.class.getClassLoader()
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
}
