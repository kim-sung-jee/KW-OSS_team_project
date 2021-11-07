package com.example.diary1;

import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.misc.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.net.URL;
import java.net.URLEncoder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class weather {

    String result;

    public static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if(nValue == null)
            return null;
        return nValue.getNodeValue();
    }
    //
    //
                        //    params progress result






    @RequiresApi(api = Build.VERSION_CODES.O)
    public String loading(String lat,String lng) throws IOException {
        //URL 가져오기
//        String service_key="1HYzBnuiZpECrx1U72F97x5pYK5j9CrcmDLy/cLRmXUmFjPkVMqW6H4ORDv0Y1hNdujMsOJjQXiiMn44Hj9FrQ==";

        String service_key="1HYzBnuiZpECrx1U72F97x5pYK5j9CrcmDLy/cLRmXUmFjPkVMqW6H4ORDv0Y1hNdujMsOJjQXiiMn44Hj9FrQ==";

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="+service_key); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("XML", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode("20211107", "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("0600", "UTF-8")); /*06시 발표(정시단위) */
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
            }  // PTY  시간 날짜 /
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        String a;

        a=result2.get("PTY"); //T1H
        a+="-";
        a+=result2.get("T1H");
        System.out.println(a);
        //return sb.toString();
        //return String.join("\n", result);
        return a;
    }


}
