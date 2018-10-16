package com.chineseall.controller;

import com.chineseall.util.FileUtil;
import com.chineseall.util.ResultUtil;
import com.chineseall.util.TiffBoxGenerator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

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

    @RequestMapping("/testTess")
    public ResponseEntity testTess(@RequestBody Map map) throws IOException {
        String textFilePath = (String) map.get("textFilePath");
        String context = FileUtil.readToString(textFilePath);
        String fontFamily = (String) map.get("fontFamily");
        int fontSize = (int) map.get("fontSize");
        Font fontGen = new Font(fontFamily, Font.PLAIN, fontSize);
        int height = (int) map.get("heigth");
        int width = (int) map.get("width");

        String outputDirectory = "/data/kangxi/";
        String fileName = "typelandkhangxidictdemo.exp0";
        float tracking = 0.0f;
        int leading = 12;
        int noise = 0;
        boolean antiAliasing = true;
        TiffBoxGenerator generator = new TiffBoxGenerator(context, fontGen, width, height);

        generator.setOutputFolder(new File(outputDirectory));
        generator.setFileName(fileName);
        generator.setTracking(tracking);
        generator.setLeading(leading);
        generator.setNoiseAmount(noise);
        generator.setAntiAliasing(antiAliasing);
        generator.create();
        return ResponseEntity.ok("success");
    }
}
