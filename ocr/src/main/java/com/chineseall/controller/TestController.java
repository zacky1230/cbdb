package com.chineseall.controller;

import com.chineseall.util.ResultUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 16:47.
 */
@RestController
public class TestController {

    @Resource
    private StringRedisTemplate template;

    @RequestMapping("/test")
    public ResponseEntity test() {
        return ResponseEntity.ok(ResultUtil.success("哈哈"));
    }

    @RequestMapping("/testRedis")
    public ResponseEntity testRedis() {

        if (!template.hasKey("shabao")) {
            template.opsForValue().append("shabao", "jjjj");
        } else {
            template.delete("shabao");
        }

        return ResponseEntity.ok("hh");
    }
}
