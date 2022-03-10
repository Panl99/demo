package com.lp.demo.api.service;

import com.alibaba.fastjson.JSONObject;
import com.lp.demo.common.enums.WeatherTypeEnum;
import com.lp.demo.common.result.JsonResult;

/**
 * @author lp
 * @date 2021/11/10 12:03
 **/
public interface WeatherService {
    /**
     * 获取经纬度当前城市天气
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @param type      气象类型
     * @param cityCode  百度城市编号
     * @return
     */
    JsonResult<JSONObject> getWeather(String longitude,
                                      String latitude,
                                      WeatherTypeEnum type,
                                      String cityCode);

    /**
     * 获取天气信息（城市id）
     *
     * @param cityId 城市id
     * @param type   获取气象类型
     * @return
     */
    JsonResult<JSONObject> getWeatherByCityId(String cityId,
                                              WeatherTypeEnum type);
}
