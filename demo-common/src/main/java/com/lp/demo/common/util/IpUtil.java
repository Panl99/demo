package com.lp.demo.common.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
     *
     * 百度开放平台接口：http://opendata.baidu.com/api.php?query=219.140.8.8&co=&resource_id=6006&oe=utf8，准确性差点
     * VORE-API：https://api.vore.top/doc/IPdata.html，准确性差点
     *
     * @link 免费ip地址信息api接口归纳：https://429006.com/article/technology/4911.htm
     *
     * @param ip IP地址
     * @return IP归属地
     */
    public static final String[] private_address = {"127.0.0.1", "localhost"};
    public static String getIpAddressByBaiduOpenData(String ip) {
        if (StringUtil.isEmpty(ip)) {
            return "";
        }
        if (Arrays.asList(private_address).contains(ip)) {
            return "私有地址";
        }
        String url = "http://opendata.baidu.com/api.php";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("query", ip);
        paramMap.put("co", "");
        paramMap.put("resource_id", 6006); // 6006 对应百度开放的IP地理位置查询服务
        paramMap.put("oe", "utf8");
        String resp;
        try {
            resp = HttpUtil.get(url, paramMap, 10 * 1000);
        } catch (Exception e) {
            System.out.println("get ip location address error! ip: " + ip + ", e: " + e.getMessage());
            return "";
        }

        return parseBaiduOpenDataResp(resp);
    }
    private static String parseBaiduOpenDataResp(String s) {
        JSONObject jo = JSONObject.parseObject(s);
        String status = jo.getString("status");
        if ("0".equalsIgnoreCase(status)) {
            JSONArray data = jo.getJSONArray("data");
            JSONObject dataJSONObject = data.getJSONObject(0);
            return dataJSONObject.getString("location");
        }
        System.out.println("Unknown get ip location address response status! response = " + s);
        return "未知";
    }

    public static String getIpAddressByVore(String ip) {
        if (StringUtil.isEmpty(ip)) {
            return "";
        }
        if (Arrays.asList(private_address).contains(ip)) {
            return "私有地址";
        }
        String url = "https://api.vore.top/api/IPdata" + ip;
        Map<String, Object> paramMap = new HashMap<>();
        String resp;
        try {
            resp = HttpUtil.get(url, paramMap, 10 * 1000);
        } catch (Exception e) {
            System.out.println("get ip location address error! ip: " + ip + ", e: " + e.getMessage());
            return "";
        }

        return parseVoreResp(resp);
    }

    private static String parseVoreResp(String s) {
        JSONObject jo = JSONObject.parseObject(s);
        String status = jo.getString("status");
        if ("0".equalsIgnoreCase(status)) {
            JSONArray data = jo.getJSONArray("data");
            JSONObject dataJSONObject = data.getJSONObject(0);
            return dataJSONObject.getString("location");
        }
        System.out.println("Unknown get ip location address response status! response = " + s);
        return "未知";
    }


    /**
     * <dependency>
     *     <groupId>com.maxmind.geoip2</groupId>
     *     <artifactId>geoip2</artifactId>
     *     <version>4.2.1</version>
     * </dependency>
     * 
     * 需要地图文件
     *         
     */
//    public static class IpLocation {
//        private DatabaseReader reader;
//
//        public IpLocation(String dbPath) throws IOException {
//            File database = new File(dbPath); // "/path/to/GeoIP2-City.mmdb"
//            this.reader = new DatabaseReader.Builder(database).build();
//        }
//
//        public String getCityLocation(String ip) {
//            try {
//                InetAddress ipAddress = InetAddress.getByName("128.101.101.101");
//
//                CityResponse response = reader.city(ipAddress);
//
//                Country country = response.getCountry();
//                System.out.println(country.getIsoCode());            // 'US'
//                System.out.println(country.getName());               // 'United States'
//                System.out.println(country.getNames().get("zh-CN")); // '美国'
//
//                Subdivision subdivision = response.getMostSpecificSubdivision();
//                System.out.println(subdivision.getName());    // 'Minnesota'
//                System.out.println(subdivision.getIsoCode()); // 'MN'
//
//                City city = response.getCity();
//                System.out.println(city.getName()); // 'Minneapolis'
//
//                Postal postal = response.getPostal();
//                System.out.println(postal.getCode()); // '55455'
//
//                Location location = response.getLocation();
//                System.out.println(location.getLatitude());  // 44.9733
//                System.out.println(location.getLongitude()); // -93.2323
//            } catch (IOException | GeoIp2Exception e) {
//                System.out.println("get ip location address error! ip: " + ip + ", e: " + e.getMessage());
//                return "未知";
//            }
//        }
//    }



    /**
     * 根据ip地址查询归属地
     * current way: https://ip-api.com/docs/api:json （秒级延迟），注意：不能商用
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
