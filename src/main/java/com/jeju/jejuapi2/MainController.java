package com.jeju.jejuapi2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeju.jejuapi2.response.*;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String callAPI(@RequestBody Map<String, Object> params) throws Exception {
        Map<String, Object> param = getParam(params);

        String station = (String) param.get("station");
//        String cityCode = (String) param.get("cityCode");
//        String nodeId = (String) param.get("nodeId");

        String a = xmlToJson(getData(station));
        Map<String, Object> pb = parseJsonToMap(a);
        Map<String, Object> response = (Map<String, Object>) pb.get("response");
        Map<String, Object> body = (Map<String, Object>) response.get("body");
        Map<String, Object> items = (Map<String, Object>) body.get("items");
        List<HashMap<String, Object>> itemList = (List<HashMap<String, Object>>) items.get("item");

        List<HashMap<String, Object>> itemList2 = xmlParser(getData(station), "//items/item");

        String result2 = "";
        String resultTest = "";
        for (HashMap<String, Object> result : itemList) {
            int arrvVhId = (int) result.get("arrvVhId");
            if (arrvVhId != 0) {
                result2 += "버스 ID: " + arrvVhId;
                result2 += " , 남은 정거장 수: " + result.get("leftStation");
                result2 += " , " + result.get("predictTravTm") + "분 전" + "\n";
                resultTest += result2;
                result2 = "";
            }
        }

        HashMap<String, Object> resultJson = new HashMap<>();

        List<HashMap<?, ?>> templateOutputs = new ArrayList<>();
        HashMap<String, Object> template = new HashMap<>();
        HashMap<String, Object> templateOutputsSimpleText = new HashMap<>();
        HashMap<String, Object> templateOutputsCarousel = new HashMap<>();

        HashMap<String, Object> carouselInfo = new HashMap<>();
        List<HashMap<?, ?>> carouselInfoItems = new ArrayList<>();
        HashMap<String, Object> imageTitle = new HashMap<>();
        HashMap<String, Object> imageTitleInfo = new HashMap<>();



        HashMap<String, Object> itemList12 = new HashMap<>();


        HashMap<String, Object> itemListIndex = new HashMap<>();
        HashMap<String, Object> itemListAlignment = new HashMap<>();
        itemListAlignment.put("itemListAlignment", "left");
        List<HashMap<String, Object>> itemListInfo = new ArrayList<>();

        HashMap<String, Object> buttons = new HashMap<>();
        List<HashMap<String, Object>> buttonsInfo = new ArrayList<>();
        HashMap<String, Object> buttonInfo = new HashMap<>();
        buttonInfo.put("label", "예약 정보");
        buttonInfo.put("action", "message");
        buttonInfo.put("messageText", "예약 정보");
        buttonsInfo.add(buttonInfo);
        buttons.put("buttons", buttonsInfo);

        HashMap<String, Object> templateOutputsSimpleTextText = new HashMap<>();
        templateOutputsSimpleTextText.put("text", resultTest);
        templateOutputsSimpleText.put("simpleText", templateOutputsSimpleTextText);

        imageTitleInfo.put("title", "버스 예정 도착 정보");
        imageTitleInfo.put("imageUrl", "https://t1.kakaocdn.net/openbuilder/docs_image/wine.jpg");

        imageTitle.put("imageTitle",imageTitleInfo);

        itemListIndex.put("title", "매장명");
        itemListIndex.put("description", "판교 A스퀘어점");
        itemListInfo.add(itemListIndex);

        itemList12.put("itemList", itemListInfo);

        carouselInfoItems.add(imageTitle);
        carouselInfoItems.add(itemList12);
        carouselInfoItems.add(buttons);
        carouselInfoItems.add(itemListAlignment);

        carouselInfo.put("type", "itemCard");
        carouselInfo.put("items", carouselInfoItems);

        templateOutputsCarousel.put("carousel", carouselInfo);

        templateOutputs.add(templateOutputsCarousel);
        templateOutputs.add(templateOutputsSimpleText);

        template.put("outputs", templateOutputs);

        resultJson.put("version", "2.0");
        resultJson.put("template", template);

        JSONObject json =  new JSONObject(resultJson);

        log.info(json);

        SkillResponse result = new SkillResponse();
        result.addCommerceCard(new CommerceCardBuilder()
                .description("따끈따끈한 보물 상자 팝니다")
                .price(10000)
                .discount(1000)
                .thumbnails(new ThumbnailBuilder()
                        .imageUrl("http://k.kakaocdn.net/dn/83BvP/bl20duRC1Q1/lj3JUcmrzC53YIjNDkqbWK/i_6piz1p.jpg")
                        .link(new LinkBuilder()
                                .web("https://store.kakaofriends.com/kr/products/1542")
                                .build())
                        .build())
                .profile(new ProfileBuilder()
                        .imageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4BJ9LU4Ikr_EvZLmijfcjzQKMRCJ2bO3A8SVKNuQ78zu2KOqM")
                        .nickname("보물상자 팝니다")
                        .build())
                .buttons(new ButtonBuilder()
                        .label("구매하기")
                        .action(EnumResponseType.ButtonActionType.WEBLINK.getTypeText())
                        .webLinkUrl("https://store.kakaofriends.com/kr/products/1542")
                        .build())
                .buttons(new ButtonBuilder()
                        .label("전화하기")
                        .action(EnumResponseType.ButtonActionType.PHONE.getTypeText())
                        .phoneNumber("354-86-00070")
                        .build())
                .buttons(new ButtonBuilder()
                        .label("공유하기")
                        .action(EnumResponseType.ButtonActionType.SHARE.getTypeText())
                        .build())
                .build());

        return result.getSkillPayload().toString();
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

    public String getData(String station) throws Exception {
        StringBuilder urlBuilder = new StringBuilder("http://busopen.jeju.go.kr/OpenAPI/service/bis/BusArrives");
        urlBuilder.append("?" + URLEncoder.encode("station", "UTF-8") + "=" + station);
//        String var10001 = URLEncoder.encode("cityCode", "UTF-8");
//        urlBuilder.append("&" + var10001 + "=" + URLEncoder.encode(cityCode, "UTF-8"));
//        var10001 = URLEncoder.encode("nodeId", "UTF-8");
//        urlBuilder.append("&" + var10001 + "=" + URLEncoder.encode(nodeId, "UTF-8"));
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
