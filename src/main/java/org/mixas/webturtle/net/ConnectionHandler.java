package org.mixas.webturtle.net;

import org.apache.log4j.Logger;
import org.mixas.webturtle.core.http.HttpRequest;
import org.mixas.webturtle.core.http.HttpRequestMethod;
import org.mixas.webturtle.core.http.HttpResponse;
import org.mixas.webturtle.core.http.HttpResponseStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Stryzhonok
 */
public class ConnectionHandler implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ConnectionHandler.class);
    private final Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        HttpRequest request = null;
        try {
            request = prepareRequest();
        } catch (IOException e) {
            LOGGER.debug("Error receiving request", e);
            try {
                sendResponse(new HttpResponse(HttpResponseStatus.BAD_REQUEST));
            } catch (IOException ex) {
                LOGGER.error("Can't send error response", ex);
            }
        }
        try {
            if (request == null) {
                sendResponse(new HttpResponse(HttpResponseStatus.BAD_REQUEST));
            } else {
                sendResponse(new HttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK,
                        Collections.<String, String>emptyMap()));
            }
        } catch (IOException e) {
            LOGGER.error("Error sending good response ", e);
            try {
                sendResponse(new HttpResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR));
            } catch (IOException ex) {
                LOGGER.error("Error sending internal server error", ex);
            }
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

    protected void sendResponse(HttpResponse response) throws IOException{
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write(response.getSendableForm());
        writer.flush();
        writer.close();
    }

}
