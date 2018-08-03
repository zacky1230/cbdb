package com.chineseall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 16:45.
 */
@SpringBootApplication
@MapperScan("com.chineseall.dao")
@EnableCaching
public class OcrApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(OcrApplication.class, args);
    }
}
