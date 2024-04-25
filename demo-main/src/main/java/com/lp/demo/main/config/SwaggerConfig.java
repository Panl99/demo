package com.lp.demo.main.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.lp.demo.common.config.BaseSwaggerConfig;
import com.lp.demo.common.model.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableKnife4j
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com/lp/demo/main/controller")
                .groupName("demo-main")
                .title("demo-main-api")
                .description("test swagger2")
                .contactName("lp")
                .contactUrl("/")
                .contactEmail("/")
                .version("/")
                .enableSecurity(false)
                .build();
    }

    /**
     * knife4j访问方式：http://ip:port/doc.html
     *
     * shiroConfig:
     *         map.put("/doc.html", "anon");
     *         map.put("/webjars/**", "anon");
     *         map.put("/swagger-resources/**", "anon");
     *         map.put("/v2/api-docs", "anon");
     *         map.put("/v2/api-docs-ext", "anon");
     *         map.put("/swagger-ui.html", "anon");
     *         map.put("/swagger-ui/index.html", "anon");
     *         map.put("/swagger/**", "anon");
     */
}