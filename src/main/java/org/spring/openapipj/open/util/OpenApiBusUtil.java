package org.spring.openapipj.open.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class OpenApiBusUtil {
    private static final String BUS_ROUTE_LIST_URL = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";
    private static final String  STATION_BY_ROUTE_URL = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStationByRoute";

    public static String getBusList(String serviceKey, String searchVal) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(BUS_ROUTE_LIST_URL); /*URL*/
        // 서비스키
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        // 노선번호
        urlBuilder.append("&" + URLEncoder.encode("strSrch","UTF-8") + "=" + URLEncoder.encode(searchVal, "UTF-8")); /*노선ID*/
        // 반환 타입
        urlBuilder.append("&resultType=json"); /*시작 정류소 순번*/

        return getBusResultData(urlBuilder.toString());

//        URL url = new URL(urlBuilder.toString());
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-type", "application/json");
//
//        System.out.println("Response code: " + conn.getResponseCode());
//        BufferedReader rd;
//
//        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        } else {
//            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//        }
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = rd.readLine()) != null) {
//            sb.append(line);
//        }
//        rd.close();
//        conn.disconnect();
//        System.out.println(sb.toString());
//
//        return sb.toString();
    }


    public static String getStationList(String serviceKey, String busRouteId) throws IOException {
        // URL
        StringBuilder urlBuilder = new StringBuilder(STATION_BY_ROUTE_URL); /*URL*/
        // ServiceKey
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        // **
        urlBuilder.append("&" + URLEncoder.encode("busRouteId","UTF-8") + "=" + URLEncoder.encode(busRouteId, "UTF-8")); /*노선ID*/
        // 반환 타입
        urlBuilder.append("&resultType=json");


        return getBusResultData(urlBuilder.toString());

//        URL url = new URL(urlBuilder.toString());
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-type", "application/json");
//        System.out.println("Response code: " + conn.getResponseCode());
//
//        BufferedReader rd;
//        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        } else {
//            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//        }
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = rd.readLine()) != null) {
//            sb.append(line);
//        }
//        rd.close();
//        conn.disconnect();
//        System.out.println(sb.toString());
//        return sb.toString();
    }


    public static String getBusResultData(String requestURL) throws IOException{

        URL url = new URL(requestURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
        return sb.toString();
    }
}
