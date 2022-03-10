package com.lp.demo.common.enums;

/**
 * @author lp
 * @date 2021/11/11 9:24
 * 阿里云墨迹天气类型枚举
 * TODO cityId版本跟经纬度版本的token不一样
 **/
public enum WeatherTypeEnum {
    /**
     * 天气实况
     */
    CONDITION("condition", "/whapi/json/aliweather/condition", "ff826c205f8f4a59701e64e9e64e01c4"),
    /**
     * 天气预报24小时
     */
    FORECAST_24_HOURS("forecast24hours", "/whapi/json/aliweather/forecast24hours", "1b89050d9f64191d494c806f78e8ea36"),
    /**
     * 天气预报15天
     */
    FORECAST_15_DAYS("forecast15days", "/whapi/json/aliweather/forecast15days", "7538f7246218bdbf795b329ab09cc524"),
    /**
     * 天气预警
     */
    ALERT("alert", "/whapi/json/aliweather/alert", "d01246ac6284b5a591f875173e9e2a18"),
    /**
     * 短时预报
     */
    SHORT_FORECAST("shortforecast", "/whapi/json/aliweather/shortforecast", "bbc0fdc738a3877f3f72f69b1a4d30fe"),
    /**
     * 空气质量指数
     */
    AQI("aqi", "/whapi/json/aliweather/aqi", "6e9a127c311094245fc1b2aa6d0a54fd"),
    /**
     * AQI预报5天
     */
    AQI_FORECAST_5_DAYS("aqiforecast5days", "/whapi/json/aliweather/aqiforecast5days", "17dbf48dff33b6228f3199dce7b9a6d6"),
    /**
     * 生活指数
     */
    INDEX("index", "/whapi/json/aliweather/index", "42b0c7e2e8d00d6e80d92797fe5360fd"),
    /**
     * 限行数据
     */
    LIMIT("limit", "/whapi/json/aliweather/limit", "c712899b393c7b262dd7984f6eb52657");

    private String type;
    private String url;
    private String token;

    WeatherTypeEnum(String type, String url, String token) {
        this.type = type;
        this.url = url;
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getToken() {
        return token;
    }

}
