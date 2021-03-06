package org.mixas.webturtle.configuration;

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

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Mikhail Stryzhonok
 */
public class XmlScenarioParser implements ScenarioParser {

    private static final String URL_NODE_NAME = "url";
    private static final String METHOD_NODE_NAME = "method";
    private static final String RESPONSE_NODE_NAME = "response";
    private static final String SOURCE_NODE_NAME = "source";
    private static final String MAPPING_NODE_NAME = "mapping";
    private static final String TYPE_NODE_NAME = "type";
    private static final String REQUESTS_NODE_NAME = "requests";
    private static final String REQUEST_NODE_NAME = "request";
    private static final String DEFAULT_XSD_PATH = "org/mixas/webturtle/scheme/validation.xsd";

    private URL xsdUrl;

    public XmlScenarioParser() {
        this(XmlScenarioParser.class.getClassLoader().getResource(DEFAULT_XSD_PATH));
    }

    /**
     * Needed for test
     * @param xsdUrl xsd url
     */
    protected XmlScenarioParser(URL xsdUrl) {
        this.xsdUrl = xsdUrl;
    }


    @Override
    public boolean isValid(String scenarioPath) {

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema;
        try {
            schema = sf.newSchema(xsdUrl);
        } catch (SAXException e) {
            throw new IllegalStateException("Cannot parse XSD schema", e);
        }
        Validator validator = schema.newValidator();
        DOMSource source = new DOMSource(prepareDocument(scenarioPath));
        try {
            validator.validate(source);
        } catch (SAXException | IOException e ) {
            return false;
        }
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
                Set<HttpRequest> requests = new HashSet<>();
                HttpResponse response = null;
                for (int j = 0; j < entries.getLength(); j++) {
                    Node entry = entries.item(j);
                    if (entry.getNodeType() == Node.ELEMENT_NODE) {
                        if (REQUESTS_NODE_NAME.equalsIgnoreCase(entry.getNodeName())) {
                            requests.addAll(parseRequests(entry));
                        } else if (RESPONSE_NODE_NAME.equalsIgnoreCase(entry.getNodeName())) {
                            response = parseResponse((Element) entry);
                        }
                    }
                }
                if (response == null) {
                    response = new HttpResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR);
                }
                System.out.println(response.getSendableForm());
                for (HttpRequest request : requests) {
                    mapping.put(request, response);
                }
            }
        }
        return mapping;
    }

    private Document prepareDocument(String validScenario) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException("Cannot initialize XML document builder", e);
        }
        Document document;
        try {
            document = builder.parse(new FileInputStream(validScenario));
        } catch (SAXException e) {
            throw new IllegalArgumentException("Cannot parse configuration file", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot open configuration file", e);
        }
        return document;
    }

    private Set<HttpRequest> parseRequests(Node requestsNode) {
        NodeList requests = requestsNode.getChildNodes();
        Set<HttpRequest> result = new HashSet<>();
        for (int i = 0; i < requests.getLength(); i ++) {
            Node request = requests.item(i);
            if (request.getNodeType() == Node.ELEMENT_NODE) {
                NodeList requestAttributes = request.getChildNodes();
                HttpRequest httpRequest = new HttpRequest();
                for (int j = 0; j < requestAttributes.getLength(); j ++) {
                    Node attribute = requestAttributes.item(j);
                    if (attribute.getNodeType() == Node.ELEMENT_NODE) {
                        if (URL_NODE_NAME.equalsIgnoreCase(attribute.getNodeName())) {
                            httpRequest.setUrl(attribute.getFirstChild().getNodeValue());
                        } else if (METHOD_NODE_NAME.equalsIgnoreCase(attribute.getNodeName())) {
                            httpRequest.setMethod(HttpRequestMethod.valueOf(attribute.getFirstChild().getNodeValue()));
                        }
                    }
                }
                result.add(httpRequest);
            }
        }
        return result;
    }

    private HttpResponse parseResponse(Element responseElement) {
        HttpResponse response = new HttpResponse();
        BodySourceType sourceType = null;
        String value = null;
        Node source = responseElement.getElementsByTagName(SOURCE_NODE_NAME).item(0);
        if (source.getNodeType() == Node.ELEMENT_NODE) {
            NodeList sourceParams = source.getChildNodes();
            for (int k = 0; k < sourceParams.getLength(); k++) {
                Node sourceParam = sourceParams.item(k);
                if (sourceParam.getNodeType() == Node.ELEMENT_NODE) {
                    if (TYPE_NODE_NAME.equalsIgnoreCase(sourceParam.getNodeName())) {
                        sourceType = BodySourceType.valueOf(sourceParam.getFirstChild().getNodeValue());
                    } else {
                        value = sourceParam.getFirstChild().getNodeValue();
                    }
                }
            }
        }
        if (sourceType == BodySourceType.FILE) {
            response.setSource(new FileResponseBodySource(value));
        } else if (sourceType == BodySourceType.STRING) {
            response.setSource(new StringResponseBodySource(value));
        } else {
            throw new IllegalStateException("Response cannot be parsed");
        }
        return response;
    }

    private enum BodySourceType {
        STRING,
        FILE
    }

}
