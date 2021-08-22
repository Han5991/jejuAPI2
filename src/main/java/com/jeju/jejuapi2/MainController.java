package com.jeju.jejuapi2;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

//    @PostMapping("/test")
//    public String Main(@RequestBody String params) throws Exception {
//        BusVo busVo = new BusVo(params);
//        String data = getData(busVo);
//        String result = xmlToJson(data);
//        log.info(result);
//
//        HashMap<String, Object> resultJson = new HashMap<>();
//
//        return "안녕하세요";
//    }

    //카카오톡 오픈빌더로 리턴할 스킬 API
    @PostMapping(value = "/test", headers = "Accept=application/json")
    public HashMap<String, Object> callAPI(@RequestBody Map<String, Object> params) throws Exception {

        HashMap<String, Object> resultJson = new HashMap<>();

            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(params);

            log.info(jsonInString);

            List<HashMap<String, Object>> outputs = new ArrayList<>();
            HashMap<String, Object> template = new HashMap<>();
            HashMap<String, Object> simpleText = new HashMap<>();
            HashMap<String, Object> text = new HashMap<>();

            text.put("text", "코딩32 발화리턴입니다.");
            simpleText.put("simpleText", text);
            outputs.add(simpleText);

            template.put("outputs", outputs);

            resultJson.put("version", "2.0");
            resultJson.put("template", template);

            log.info(resultJson);

        return resultJson;
    }

    public String getData(BusVo params) throws Exception {

        StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList");
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + params.getServiceKey());
        String var10001 = URLEncoder.encode("cityCode", "UTF-8");
        urlBuilder.append("&" + var10001 + "=" + URLEncoder.encode(params.getCityCode(), "UTF-8"));
        var10001 = URLEncoder.encode("nodeId", "UTF-8");
        urlBuilder.append("&" + var10001 + "=" + URLEncoder.encode(params.getNodeId(), "UTF-8"));
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

    public String xmlToJson(String xml) {
        JSONObject xmlJSONObj = XML.toJSONObject(xml);
        return xmlJSONObj.toString(4);
    }
}
