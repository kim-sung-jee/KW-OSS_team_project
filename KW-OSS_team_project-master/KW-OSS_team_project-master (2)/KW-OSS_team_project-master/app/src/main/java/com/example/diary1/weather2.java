package com.example.diary1;

import android.os.Build;
import android.os.StrictMode;

import androidx.annotation.RequiresApi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class weather2 {
    public static String getTagValue(String tag, Element eElement){
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if(nValue == null)
            return null;
        return nValue.getNodeValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String loading() throws IOException {
        //URL 가져오기
        String service_key="1HYzBnuiZpECrx1U72F97x5pYK5j9CrcmDLy/cLRmXUmFjPkVMqW6H4ORDv0Y1hNdujMsOJjQXiiMn44Hj9FrQ==";

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="+service_key); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("XML", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode("20211106", "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("0600", "UTF-8")); /*06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("55", "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("127", "UTF-8")); /*예보지점의 Y 좌표값*/

        //Xml코드 가져오기
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL url = new URL(urlBuilder.toString());
        /*HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        conn.setConnectTimeout(3000);
        int a=conn.getResponseCode();
        BufferedReader rd;

        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);//전부 다 저장
        }
        rd.close();
        conn.disconnect();*/
        //파싱
        DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;

        ArrayList<String> result=new ArrayList<>();//결과 값
        try {
            dBuilder = dbFactoty.newDocumentBuilder();
            Document doc = null;

            doc = dBuilder.parse(urlBuilder.toString());
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("item");

            for(int temp=0;temp< nList.getLength();temp++){
                Node nNode= nList.item(temp);
                if(nNode.getNodeType()==Node.ELEMENT_NODE){
                    Element eElement=(Element)nNode;
                    if(temp==0) {
                        result.add(getTagValue("baseDate", eElement));
                        result.add(getTagValue("baseTime", eElement));
                        result.add(getTagValue("nx", eElement));
                        result.add(getTagValue("ny", eElement));
                    }
                    result.add(getTagValue("category", eElement));
                    result.add(getTagValue("obsrValue", eElement));
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }


        //return sb.toString();
        return String.join("\n", result);
    }
}
