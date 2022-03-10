package com.lp.demo.api.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.lp.demo.api.service.WeatherService;
import com.lp.demo.common.enums.WeatherTypeEnum;
import com.lp.demo.common.result.JsonResult;
import com.lp.demo.common.result.ResultEnum;
import com.lp.demo.common.util.AliyunHttpUtils;
import com.lp.demo.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lp
 * @date 2021/11/10 13:45
 **/
@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Override
    public JsonResult<JSONObject> getWeather(String longitude, String latitude, WeatherTypeEnum type, String cityCode) {
        JsonResult<JSONObject> result = new JsonResult<>();
        if (StringUtil.isEmpty(longitude) || StringUtil.isEmpty(latitude)) {
            log.error("lat or lon is empty!");
            return new JsonResult<>(ResultEnum.FAIL);
        }

        if (type == null) {
            log.error("type is null!");
            return new JsonResult<>(ResultEnum.FAIL);
        }

        if (StringUtil.isEmpty(cityCode)) {
            // 根据经纬度查出当前城市
            JSONObject geocoding = getGeocoding(latitude, longitude);
            if (geocoding == null || !geocoding.containsKey("result")) {
                log.error("get geocoding failed! [lat:{},lon:{}]", latitude, longitude);
                return new JsonResult<>(ResultEnum.FAIL);
            }
            // 注意：百度查出的cityCode跟阿里墨迹查出的cityId不一样
            cityCode = String.valueOf(geocoding.getJSONObject("result").getIntValue("cityCode"));
            log.info("current location: {}, cityCode = {}", geocoding.getJSONObject("result").getString("formatted_address"), cityCode);
        }
        String cityWeatherKey = "cityCode-" + cityCode + "-" + type.getType();

        // 获取当前城市天气
        String weatherCache = (String) redisTemplate.opsForValue().get(cityWeatherKey);
        JSONObject data = JSONObject.parseObject(weatherCache);
        if (data == null || data.isEmpty()) {
            String response = getWeather(type.getUrl(), type.getToken(), longitude, latitude);
            JSONObject object = JSONObject.parseObject(response);
            if (object == null || object.getInteger("code") != 0) {
                log.error("get weather failed! response = {}", response);
                return new JsonResult<>(ResultEnum.FAIL);
            }
            data = object.getJSONObject("data");
            if (data == null || data.isEmpty()) {
                log.error("get weather failed! data is empty! response = {}", response);
                return new JsonResult<>(ResultEnum.FAIL);
            }

            if (type == WeatherTypeEnum.FORECAST_15_DAYS || type == WeatherTypeEnum.AQI_FORECAST_5_DAYS) {
                redisTemplate.opsForValue().set(cityWeatherKey, data.toJSONString(), 8, TimeUnit.HOURS);
            } else {
                redisTemplate.opsForValue().set(cityWeatherKey, data.toJSONString(), 1, TimeUnit.HOURS);
            }
        }
        result.setData(data);
        return result;
    }

    @Override
    public JsonResult<JSONObject> getWeatherByCityId(String cityId, WeatherTypeEnum type) {
        // TODO 注意：cityId版本跟经纬度版本的token不一样
        return null;
    }


    /**
     * 根据经纬度获取城市信息
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return
     */
    private static final String BAIDU_MAP_URL = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=%s&output=json&coordtype=wgs84ll&location=%s,%s";
    private static final String BAIDU_MAP_AK = "";// TODO get AK

    public static JSONObject getGeocoding(String latitude, String longitude) {
        String url = String.format(BAIDU_MAP_URL, BAIDU_MAP_AK, latitude, longitude);
        JSONObject json = null;
        try {
            String response = HttpUtil.get(url);
            json = JSONObject.parseObject(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * @param path      天气类型路径
     * @param token     墨迹天气api固定token,参见WeatherTypeEnum
     * @param longitude 经度
     * @param latitude  纬度
     * @return
     */
    public static String getWeather(String path, String token, String longitude, String latitude) {
        String host = "http://aliv8.data.moji.com";
        String method = "POST";
        String appcode = "";// TODO get APPCODE
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Authorization", "APPCODE " + appcode);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("lat", latitude);
        bodys.put("lon", longitude);
        bodys.put("token", token);
        try {
            HttpResponse response = AliyunHttpUtils.doPost(host, path, method, headers, querys, bodys);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
