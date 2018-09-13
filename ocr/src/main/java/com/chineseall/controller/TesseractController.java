package com.chineseall.controller;

import com.alibaba.fastjson.JSONObject;
import com.chineseall.service.TesseractService;
import com.chineseall.util.RetMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity getBoxData(@RequestBody JSONObject jsonParam) throws IOException {
        RetMsg retMsg = new RetMsg();
        tesseractService.saveBoxToFile(jsonParam);
        return ResponseEntity.ok(retMsg);
    }

    @RequestMapping("/trainBox/{t}")
    public ResponseEntity trainBox(@PathVariable String t){
        RetMsg retMsg = new RetMsg();
        tesseractService.trainBox(t);
        return ResponseEntity.ok(retMsg);
    }

}
