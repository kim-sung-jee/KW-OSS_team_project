package org.example.myapplication;

import android.os.Build;
import android.os.StrictMode;

import androidx.annotation.RequiresApi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//날씨 불러오기
public class setWeather {
    //특정 태그만 추출
    public static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if(nValue == null)
            return null;
        return nValue.getNodeValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String loading(String lat,String lng,String date,String hour) throws IOException {
        //URL 가져오기
        String service_key="";

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="+service_key); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("XML", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(hour+"00", "UTF-8")); /*06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(lng, "UTF-8")); /*예보지점의 Y 좌표값*/
        System.out.println(lat+""+lng);

        //Xml코드 가져오기
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        System.out.println(urlBuilder.toString());
        DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;


        HashMap<String,String> result2=new HashMap<String,String>();
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

                    result2.put(getTagValue("category", eElement),
                            getTagValue("obsrValue", eElement));


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //PTY, T1H 추출
        String a;

        a=result2.get("PTY");
        a+="-";
        a+=result2.get("T1H");
        System.out.println(a);

        return a;
    }
}
