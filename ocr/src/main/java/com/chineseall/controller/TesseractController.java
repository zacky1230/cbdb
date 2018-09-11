package com.chineseall.controller;

import com.alibaba.fastjson.JSONArray;
import com.chineseall.service.TesseractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 13:55.
 */
@RestController
@RequestMapping("/ocr")
public class TesseractController {

    @Autowired
    private TesseractService tesseractService;

    @RequestMapping("/getBoxData")
    public ResponseEntity getBoxData(HttpServletRequest request, @RequestBody JSONArray jsonParam) throws IOException {
        tesseractService.saveBoxToFile(jsonParam);
        return ResponseEntity.ok(100);
    }

}
