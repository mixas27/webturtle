package org.mixas.webturtle.configuration;

import org.apache.log4j.Logger;
import org.mixas.webturtle.core.http.request.HttpRequest;
import org.mixas.webturtle.core.http.request.HttpRequestMethod;
import org.mixas.webturtle.core.http.response.FileResponseBodySource;
import org.mixas.webturtle.core.http.response.HttpResponse;
import org.mixas.webturtle.core.http.response.HttpResponseStatus;
import org.mixas.webturtle.core.http.response.StringResponseBodySource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Stryzhonok
 */
public class XmlScenarioParser implements ScenarioParser {
    private static final Logger LOGGER = Logger.getLogger(XmlScenarioParser.class);

    private static final String URL_NODE_NAME = "url";
    private static final String METHOD_NODE_NAME = "method";
    private static final String RESPONSE_NODE_NAME = "response";
    private static final String SOURCE_NODE_NAME = "source";
    private static final String FILE_TYPE = "FILE";
    private static final String MAPPING_NODE_NAME = "mapping";

    @Override
    public boolean isValid(String scenarioPath) {
        return true;
    }

    @Override
    public Map<HttpRequest, HttpResponse> parse(String validScenario) {
        Map<HttpRequest, HttpResponse> mapping = new HashMap<>();
        Document document = prepareDocument(validScenario);
        NodeList nodeList = document.getElementsByTagName(MAPPING_NODE_NAME);
        for (int i = 0; i < nodeList.getLength(); i ++) {
            Node mappingNode = nodeList.item(i);
            if (mappingNode.getNodeType() == Node.ELEMENT_NODE) {
                NodeList entries = mappingNode.getChildNodes();
                HttpRequest request = new HttpRequest();
                HttpResponse response = null;
                for (int j = 0; j < entries.getLength(); j++) {
                    Node entry = entries.item(j);
                    if (entry.getNodeType() == Node.ELEMENT_NODE) {
                        if (URL_NODE_NAME.equalsIgnoreCase(entry.getNodeName())) {
                            request.setUrl(entry.getFirstChild().getNodeValue());
                        } else if (METHOD_NODE_NAME.equalsIgnoreCase(entry.getNodeName())) {
                            request.setMethod(HttpRequestMethod.valueOf(entry.getFirstChild().getNodeValue()));
                        } else if (RESPONSE_NODE_NAME.equalsIgnoreCase(entry.getNodeName())) {
                            response = parseResponse((Element) entry);
                        }
                    }
                }
                System.out.println(response.getSendableForm());
                if (response == null) {
                    response = new HttpResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR);
                }
                mapping.put(request, response);
            }
        }
        return mapping;
    }

    private Document prepareDocument(String validScenario) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException("Cannot initialize XML document builder", e);
        }
        Document document = null;
        try {
            document = builder.parse(new FileInputStream(validScenario));
        } catch (SAXException e) {
            throw new IllegalArgumentException("Cannot parse configuration file", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot open configuration file", e);
        }
        return document;
    }

    private HttpResponse parseResponse(Element responseElement) {
        HttpResponse response = new HttpResponse();
        Node source = responseElement.getElementsByTagName(SOURCE_NODE_NAME).item(0);
        if (source.getNodeType() == Node.ELEMENT_NODE) {
            NodeList sourceParams = source.getChildNodes();
            for (int k = 0; k < sourceParams.getLength(); k++) {
                Node sourceParam = sourceParams.item(k);
                if (sourceParam.getNodeType() == Node.ELEMENT_NODE) {
                    if (FILE_TYPE.equalsIgnoreCase(sourceParam.getNodeName())) {
                        response.setSource(new FileResponseBodySource(sourceParam.getFirstChild().getNodeValue()));
                    } else {
                        response.setSource(new StringResponseBodySource(sourceParam.getFirstChild().getNodeValue()));
                    }
                }
            }
        }
        return response;
    }

}
