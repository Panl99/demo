package com.lp.demo.main;

import com.lp.demo.brcc.DemoBrccApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author lp
 */
@SpringBootApplication
@Import({DemoBrccApplication.class})
public class DemoMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoMainApplication.class, args);
    }

}
