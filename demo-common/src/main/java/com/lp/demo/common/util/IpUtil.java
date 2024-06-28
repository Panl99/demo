package com.lp.demo.common.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lp
 * @date 2024/6/26 11:23
 * @desc
 **/
public class IpUtil {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
//        System.out.println("startTime = " + startTime);
        String ip = "47.98.248.200";
//        String location = getLocationByIP(ip);
//        System.out.println(location);
//        {
//            "status": "success",
//            "country": "中国",
//            "countryCode": "CN",
//            "region": "ZJ",
//            "regionName": "浙江省",
//            "city": "杭州",
//            "zip": "",
//            "lat": 30.2994,
//            "lon": 120.1612,
//            "timezone": "Asia/Shanghai",
//            "isp": "Hangzhou Alibaba Advertising Co",
//            "org": "Aliyun Computing Co., LTD",
//            "as": "AS37963 Hangzhou Alibaba Advertising Co.,Ltd.",
//            "query": "47.98.248.200"
//        }

        String address = getAddressByIp(ip);
        System.out.println("address = " + address);
        long endTime = System.currentTimeMillis();
//        System.out.println("endTime = " + endTime);
        System.out.println("time = " + convertMillisToTime(endTime - startTime));

//        {
//            "status": "success",
//            "country": "中国",
//            "regionName": "浙江省",
//            "city": "杭州"
//        }
    }

    public static String convertMillisToTime(long millis) {
        long milliseconds = millis % 1000;
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds %= 60;
        minutes %= 60;
        hours %= 24;

        return String.format("%02d:%02d:%02d:%03d", hours, minutes, seconds, milliseconds);
    }

    /**
     * 根据ip地址查询归属地
     * current way: https://ip-api.com/docs/api:json （秒级延迟）
     * anther way: https://github.com/lionsoul2014/ip2region （自称0.x毫秒、离线）
     *
     * @param ip IP地址
     * @return IP归属地
     */
    public static String getAddressByIp(String ip) {
        String url = "http://ip-api.com/json/" + ip;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("lang", "zh-CN");
        paramMap.put("fields", "status,message,country,regionName,city");
        String resp = HttpUtil.get(url, paramMap, 10 * 1000);
        return parseAddress(resp);
    }

    private static String parseAddress(String s) {
        JSONObject jo = JSONObject.parseObject(s);
        String status = jo.getString("status");
        if ("fail".equalsIgnoreCase(status)) {
            System.out.println("get ip location address fail! response = " + s);
            String message = jo.getString("message");
            if ("private range".equalsIgnoreCase(message)) {
                return "私有地址";
            }
            if ("reserved range".equalsIgnoreCase(message)) {
                return "保留地址";
            }
            if ("invalid query".equalsIgnoreCase(message)) {
                return "无效地址";
            }
            return "未知";
        }
        if ("success".equalsIgnoreCase(status)) {
            String country = jo.getString("country");
            String regionName = jo.getString("regionName");
            String city = jo.getString("city");
            return country + "-" + regionName + "-" + city;
        }
        System.out.println("Unknown get ip location address response status! response = " + s);
        return "未知";
    }

    public static String getLocationByIP(String ip) {
        String urlString = "http://ip-api.com/json/"
                + ip
                + "?lang=zh-CN";
        URL url;
        URLConnection connection;
        BufferedReader in = null;
        String inputLine;
        StringBuilder response = new StringBuilder();

        try {
            url = new URL(urlString);
            connection = url.openConnection();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (Exception e) {
            System.out.println("e = " + e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("e = " + e);
                }
            }
        }
        return response.toString();
    }


    public static String getClientIp() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        return servletRequestAttributes.getRequest().getHeader("X-Real-IP");
    }

}
