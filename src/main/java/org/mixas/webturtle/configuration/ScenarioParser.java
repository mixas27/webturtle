package org.mixas.webturtle.configuration;

import org.mixas.webturtle.core.http.request.HttpRequest;
import org.mixas.webturtle.core.http.response.HttpResponse;

import java.util.Map;

/**
 * @author Mikhail Stryzhonok
 */
public interface ScenarioParser {

    boolean isValid(String scenarioPath);

    Map<HttpRequest, HttpResponse> parse(String validScenario);
}
