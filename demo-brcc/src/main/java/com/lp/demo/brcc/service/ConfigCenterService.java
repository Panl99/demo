package com.lp.demo.brcc.service;

import com.baidu.brcc.spring.ConfigCenterPropertyPlaceholderConfigurer;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lp
 * @date 2021/7/1 16:06
 **/
@Service
public class ConfigCenterService {
    private static final String CC_SERVER_URL = "http://localhost:8086";
    private static final String PROJECT_NAME = "Demo";
    private static final String PROJECT_API_PASSWORD = "123456";
    private static final String ENV_NAME = "dev";
    private static final long ENV_ID = 1;
    private static final String VERSION_NAME = "1.0.0";
    private static final long VERSION_ID = 1;

    public void createConfig() {
        ConfigCenterPropertyPlaceholderConfigurer configurer = new ConfigCenterPropertyPlaceholderConfigurer();
        configurer.setCcServerUrl(CC_SERVER_URL);
        configurer.setProjectName(PROJECT_NAME);
        configurer.setCcPassword(PROJECT_API_PASSWORD);
        configurer.setEnvName(ENV_NAME);
//        configurer.setCcEnvId(ENV_ID);
        configurer.setCcVersionName(VERSION_NAME);
//        configurer.setCcVersion(VERSION_ID);

        Map<String, String> properties = configurer.getProperties();
        System.out.println(properties);


    }
}
