package org.mixas.webturtle.net;

import org.mixas.webturtle.core.HttpRequest;
import org.mixas.webturtle.core.HttpRequestMethod;
import org.mixas.webturtle.core.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Stryzhonok
 */
public class ConnectionHandler implements Runnable {
    private final Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            HttpRequest request = prepareRequest();
        } catch (IOException e) {

        }
    }

    private Map<String, String> readHeaders(BufferedReader reader) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] pair = line.split(":");
            headers.put(pair[0].trim(), pair[1].trim());
        }
        return headers;
    }

    protected HttpRequest prepareRequest() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String requestLine  = reader.readLine();
        Map<String, String> headers = Collections.emptyMap();
        if (requestLine == null) {
            throw new IOException();
        }

        String[] requestLineEntries = requestLine.split(" ");
        if (requestLineEntries.length != 3) {
            throw new IOException();
        }

        return new HttpRequest(HttpRequestMethod.valueOf(requestLineEntries[0].trim()), requestLineEntries[1].trim(),
                requestLineEntries[2].trim(), headers);
    }

    protected void sendResponse(HttpResponse response) {

    }

}
