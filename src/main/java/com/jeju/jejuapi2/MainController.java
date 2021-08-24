package com.jeju.jejuapi2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
public class MainController {

    //카카오톡 오픈빌더로 리턴할 스킬 API
    @PostMapping(value = "/test", headers = "Accept=application/json")
    public HashMap<String, Object> callAPI(@RequestBody Map<String, Object> params) throws Exception {

        Map<String, Object> param = getParam(params);

        String ServiceKey = (String) param.get("ServiceKey");
        String cityCode = (String) param.get("cityCode");
        String nodeId = (String) param.get("nodeId");

        String a = xmlToJson(getData(ServiceKey, cityCode, nodeId));
        Map<String, Object> pb = parseJsonToMap(a);
        Map<String, Object> response = (Map<String, Object>) pb.get("response");
        Map<String, Object> body = (Map<String, Object>) response.get("body");
        Map<String, Object> items = (Map<String, Object>) body.get("items");
        List<HashMap<String, Object>> itemList = (List<HashMap<String, Object>>) items.get("item");

        List<HashMap<String, Object>> itemList2 = xmlParser(getData(ServiceKey, cityCode, nodeId), "//items/item");

        String aaa = "";
        for (HashMap<String, Object> result : itemList2) {
            aaa += result.get("arrtime");
        }

        HashMap<String, Object> resultJson = new HashMap<>();

        List<HashMap<String, Object>> outputs = new ArrayList<>();
        HashMap<String, Object> template = new HashMap<>();
        HashMap<String, Object> simpleText = new HashMap<>();
        HashMap<String, Object> text = new HashMap<>();

        text.put("text", aaa);
        simpleText.put("simpleText", text);
        outputs.add(simpleText);

        template.put("outputs", outputs);

        resultJson.put("version", "2.0");
        resultJson.put("template", template);

        return resultJson;
    }

    public String xmlToJson(String xml) {
        JSONObject xmlJSONObj = XML.toJSONObject(xml);
        return xmlJSONObj.toString(4);
    }

    public Map<String, Object> parseJsonToMap(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<>() {
        });
    }

    public List<HashMap<String, Object>> xmlParser(String xml, String expression) throws Exception {

        //DocumentBuilderFactory 생성
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        // xml 파싱하기
        InputSource is = new InputSource(new StringReader(xml));
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(is);
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        XPathExpression expr = xpath.compile(expression);
        NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

        List<HashMap<String, Object>> itemList = new ArrayList<>();
        HashMap<String, Object> item = new HashMap<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            NodeList child = nodeList.item(i).getChildNodes();
            for (int j = 0; j < child.getLength(); j++) {
                Node node = child.item(j);
                item.put(node.getNodeName(), node.getTextContent());
            }
            itemList.add(item);
        }
        return itemList;
    }

    public String getData(String ServiceKey, String cityCode, String nodeId) throws Exception {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList");
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + ServiceKey);
        String var10001 = URLEncoder.encode("cityCode", "UTF-8");
        urlBuilder.append("&" + var10001 + "=" + URLEncoder.encode(cityCode, "UTF-8"));
        var10001 = URLEncoder.encode("nodeId", "UTF-8");
        urlBuilder.append("&" + var10001 + "=" + URLEncoder.encode(nodeId, "UTF-8"));
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        return sb.toString();
    }

    public Map<String, Object> getParam(Map<String, Object> params) {
        return (Map<String, Object>) (((Map<?, ?>) params.get("action")).get("params"));
    }
}
