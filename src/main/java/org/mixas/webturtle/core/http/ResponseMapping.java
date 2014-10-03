package org.mixas.webturtle.core.http;

import org.mixas.webturtle.core.http.request.HttpRequest;
import org.mixas.webturtle.core.http.response.HttpResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Stryzhonok
 */
public class ResponseMapping {

    private final Map<HttpRequest, HttpResponse> mapping;
    private final Map<HttpRequest, Integer> statistic;

    public ResponseMapping(Map<HttpRequest, HttpResponse> mapping) {
        this.mapping = mapping;
        statistic = new HashMap<>();
        for (HttpRequest request : mapping.keySet()) {
            statistic.put(request, 0);
        }
    }

    public HttpResponse getResponse(HttpRequest request) {
        increaseRequestCounter(request);
        return mapping.get(request);
    }

    private synchronized void increaseRequestCounter(HttpRequest request) {
        Integer requestStat = statistic.get(request);
        if (requestStat != null) {
            requestStat ++;
            statistic.put(request, requestStat);
        }
    }

    public synchronized Integer getStatisticForRequest(HttpRequest request) {
        return statistic.get(request);
    }

    public synchronized Map<HttpRequest, Integer> getAllStatistic() {
        return Collections.unmodifiableMap(statistic);
    }

}
