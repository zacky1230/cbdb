package com.chineseall.controller;

import com.chineseall.util.ResultUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 16:47.
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    public ResponseEntity test() {
        return ResponseEntity.ok(ResultUtil.success("哈哈"));
    }
}
