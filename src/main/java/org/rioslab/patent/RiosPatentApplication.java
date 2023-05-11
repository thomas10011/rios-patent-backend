package org.rioslab.patent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@MapperScan("org.rioslab.patent.mapper")
public class RiosPatentApplication {
    public static void main(String[] args) {
        SpringApplication.run(RiosPatentApplication.class, args);
    }
}