package com.lp.demo.api.controller;


import com.lp.demo.api.service.WeatherService;
import com.lp.demo.common.enums.WeatherTypeEnum;
import com.lp.demo.common.result.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author lp
 * @date 2021/11/8 15:00
 **/
@Slf4j
@RestController
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    /**
     * 获取天气信息（经纬度）
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @param type      获取气象类型
     * @param cityCode  百度城市编号
     * @return
     */
    @GetMapping("/demo/weather/{type}")
    public JsonResult getWeather(@RequestParam("longitude") String longitude,
                                 @RequestParam("latitude") String latitude,
                                 @PathVariable WeatherTypeEnum type,
                                 String cityCode) {
        log.info("Get weather by latitude and longitude, longitude = {}, latitude = {}, type = {}, cityCode = {}", longitude, latitude, type, cityCode);
        return weatherService.getWeather(longitude, latitude, type, cityCode);
    }

    /**
     * 获取天气信息（城市id）
     *
     * @param cityId  城市编号
     * @param type      获取气象类型
     * @return
     */
    @GetMapping("/demo/weather/{type}")
    public JsonResult getWeather(@RequestParam("cityId") String cityId,
                                 @PathVariable WeatherTypeEnum type) {
        log.info("Get weather by cityId, cityId = {}, type = {}", cityId, type);
        return weatherService.getWeatherByCityId(cityId, type);
    }

    /**
     * 接口参数格式化
     * 作用：支持接口枚举参数小写格式
     *
     * @param registry
     */
    @Autowired
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, WeatherTypeEnum.class, source -> WeatherTypeEnum.valueOf(source.toUpperCase()));
    }
}
