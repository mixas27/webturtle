package org.mixas.webturtle.configuration;

import org.mixas.webturtle.core.http.request.HttpRequest;
import org.mixas.webturtle.core.http.request.HttpRequestMethod;
import org.mixas.webturtle.core.http.response.FileResponseBodySource;
import org.mixas.webturtle.core.http.response.HttpResponse;
import org.mixas.webturtle.core.http.response.StringResponseBodySource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Stryzhonok
 */
public class XmlScenarioParser implements ScenarioParser {
    private static final String URL_NODE_NAME = "url";
    private static final String METHOD_NODE_NAME = "method";
    private static final String RESPONSE_NODE_NAME = "response";
    private static final String SOURCE_NODE_NAME = "source";
    private static final String TYPE_NODE_NAME = "type";
    private static final String VALUE_NODE_NAME = "value";
    private static final String FILE_TYPE = "FILE";
    private static final String STRING_TYPE = "STRING";

    @Override
    public boolean isValid(String scenarioPath) {
        return true;
    }

    @Override
    public Map<HttpRequest, HttpResponse> parse(String validScenario) {
        Map<HttpRequest, HttpResponse> mapping = new HashMap<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new FileInputStream(validScenario));
            NodeList nodeList = document.getElementsByTagName("mapping");
            System.out.println(nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i ++) {
                Node mappingNode = nodeList.item(i);
                if (!(mappingNode instanceof Element)) {
                    continue;
                }
                NodeList entries = mappingNode.getChildNodes();
                HttpRequest request = new HttpRequest();
                HttpResponse response = new HttpResponse();
                for (int j = 0; j < entries.getLength(); j ++) {
                    Node entry = entries.item(j);
                    if (!(entry instanceof Element)) {
                        continue;
                    }
                    if (URL_NODE_NAME.equalsIgnoreCase(entry.getNodeName())) {
                        request.setUrl(entry.getFirstChild().getNodeValue());
                    } else if (METHOD_NODE_NAME.equalsIgnoreCase(entry.getNodeName())) {
                        String value = entry.getFirstChild().getNodeValue();
                        request.setMethod(HttpRequestMethod.valueOf(value));
                    } else if (RESPONSE_NODE_NAME.equalsIgnoreCase(entry.getNodeName())) {
                        Node source = ((Element) entry).getElementsByTagName(SOURCE_NODE_NAME).item(0);
                        if (source.getNodeType() == Node.ELEMENT_NODE) {
                            NodeList sourceParams = source.getChildNodes();
                            for (int k = 0; k < sourceParams.getLength(); k ++) {
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

                   } else {
                        //throw new IllegalArgumentException();
                    }
                }
                System.out.println(response.getSendableForm());
                mapping.put(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapping;
    }

}
