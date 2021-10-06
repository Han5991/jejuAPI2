package com.jeju.jejuapi2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jeju.jejuapi2.response.*;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
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
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
public class MainController {

    //카카오톡 오픈빌더로 리턴할 스킬 API
//    @PostMapping(value = "/test", headers = "Accept=application/json")
//    public String callAPI(@RequestBody Map<String, Object> params) throws Exception {
//        Map<String, Object> param = getParam(params);
//
//        String station = (String) param.get("stationNm");
//
//        String a = xmlToJson(getData("2"));
//        Map<String, Object> pb = parseJsonToMap(a);
//        Map<String, Object> response = (Map<String, Object>) pb.get("response");
//        Map<String, Object> body = (Map<String, Object>) response.get("body");
//        Map<String, Object> items = (Map<String, Object>) body.get("items");
//        List<HashMap<String, Object>> itemList = (List<HashMap<String, Object>>) items.get("item");
//
//        String result2 = "";
//        String resultTest = "";
//        int i = 0;
//        for (HashMap<String, Object> result : itemList) {
//            String stationNm = (String) result.get("stationNm");
//            int stationId = (int) result.get("stationId");
//            System.out.println();
//            if (stationNm.equals(station) && !(stationNm.length() == 0)) {
//                result2 += "버스 이름: " + stationNm + ", 버스 아이디: " + stationId + "\n";
//                resultTest += result2;
//                result2 = "";
//            }
//
//        }
//
//        SkillResponse result = new SkillResponse();
//        result.addSimpleText(resultTest);
//
//        JSONArray array = new JSONArray();
//
//
//        return result.getSkillPayload().toString();
//    }

    public static String xmlToJson(String xml) {
        JSONObject xmlJSONObj = XML.toJSONObject(xml);
        return xmlJSONObj.toString(4);
    }

    public static Map<String, Object> parseJsonToMap(String json) throws JsonProcessingException {
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

    public static String getData(int number) throws Exception {
        URL url = new URL("https://open.jejudatahub.net/api/proxy/DD11ab6a6t11D16baaa1a2tD26ata161/4_6060cteceoo4c_e464octr4e4t_6rr?limit=100&number="+number);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

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

    public static void main(String[] args) throws Exception {

//        String result2 = "";
//        String resultTest = "";
//        List <String> stringList = new ArrayList<>();
//
//        for(int i = 1; i<=40;i++){
//            Map<String, Object> pb = parseJsonToMap(getData(i));
//
//            List<HashMap<String, Object>> itemList = (List<HashMap<String, Object>>) pb.get("data");
//
//            for(HashMap<String, Object> a : itemList){
//                if(!result2.contains((String) a.get("stationName"))){
//                    result2 +=a.get("stationName")+"\n";
//                    stringList.add((String)a.get("stationName"));
//                }
//            }
//        }

//        for(HashMap<String, Object> a : itemList){
//            stringList.add((String) a.get("title"));
////            String stationNm = ;
////            result2 += stationNm+"\n";
////            resultTest += result2;
////            result2 = "";
//        }
//
////        int b = (Integer.parseInt((String)pb.get("totalCount"))%100==0) ? Integer.parseInt((String)pb.get("totalCount"))/100 : Integer.parseInt((String)pb.get("totalCount"))/100 + 1;
//        for(int i = 2; i<13;i++){
//            pb = parseJsonToMap(getData());
//            itemList = (List<HashMap<String, Object>>) pb.get("items");
//            for(HashMap<String, Object> a : itemList){
//                stringList.add((String) a.get("title"));
////                result2 += stationNm+"\n";
////                resultTest += result2;
////                result2 = "";
//            }
//        }
//
//
        writeExcelFile();
//        File file = new File("test1.txt");
//        FileWriter writer = null;
//
//        try {
//            writer = new FileWriter(file, false);
//            writer.write(result2);
//            writer.flush();
//        } catch(IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if(writer != null) writer.close();
//            } catch(IOException e) {
//                e.printStackTrace();
//            }
//        }

//        System.out.println(itemList);
    }

    public static void writeExcelFile() throws EncryptedDocumentException, IOException {

        FileInputStream file = new FileInputStream("C:\\Users\\USER\\IdeaProjects\\jejuAPI10\\제주버스정류장 목록2.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        int rowindex=0;
        int columnindex=0;
        ArrayList <String> list = new ArrayList<>();
        //시트 수 (첫번째에만 존재하므로 0을 준다)
        //만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
        XSSFSheet sheet=workbook.getSheetAt(0);
        //행의 수
        int rows=sheet.getPhysicalNumberOfRows();
        for(rowindex=0;rowindex<rows;rowindex++){
            //행을읽는다
            XSSFRow row=sheet.getRow(rowindex);
            if(row !=null){
                //셀의 수
                int cells=row.getPhysicalNumberOfCells();
                for(columnindex=0; columnindex<=cells; columnindex++){
                    //셀값을 읽는다
                    XSSFCell cell=row.getCell(columnindex);
                    String value="";
                    //셀이 빈값일경우를 위한 널체크
                    if(cell==null){
                        continue;
                    }else{
                        //타입별로 내용 읽기
                        switch (cell.getCellType()){
                            case XSSFCell.CELL_TYPE_FORMULA:
                                value=cell.getCellFormula();
                                break;
                            case XSSFCell.CELL_TYPE_NUMERIC:
                                value=cell.getNumericCellValue()+"";
                                break;
                            case XSSFCell.CELL_TYPE_STRING:
                                value=cell.getStringCellValue()+"";
                                break;
                            case XSSFCell.CELL_TYPE_BLANK:
                                value=cell.getBooleanCellValue()+"";
                                break;
                            case XSSFCell.CELL_TYPE_ERROR:
                                value=cell.getErrorCellValue()+"";
                                break;
                        }
                    }
                    list.add(value);
                }

            }
        }

        String filePath = "제주버스정류장 목록.xlsx";    // 저장할 파일 경로

        FileOutputStream fos = new FileOutputStream(filePath);

        sheet = workbook.createSheet("stationNameList");    // sheet 생성

        XSSFRow curRow;

        int row = list.size();    // list 크기
        System.out.println(row);
        curRow = sheet.createRow(0);    // row 생성
        for (int i = 0; i < row-1; i++) {
            curRow.createCell(i).setCellValue(list.get(i));
        }

        workbook.write(fos);
        fos.close();
        System.out.println("ok");
    }
}
