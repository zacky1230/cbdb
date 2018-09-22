package com.chineseall.controller;

import com.alibaba.fastjson.JSONObject;
import com.chineseall.service.TesseractService;
import com.chineseall.util.RetMsg;
import com.chineseall.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

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
        boolean success = tesseractService.saveBoxToFile(jsonParam);
        if (success) {
            retMsg.success("ok");
            return ResponseEntity.ok(retMsg);
        } else {
            retMsg.fail();
            return ResponseEntity.ok(retMsg);
        }

    }

    @RequestMapping("/trainBox/{t}")
    public ResponseEntity trainBox(@PathVariable String t) {
        RetMsg retMsg = new RetMsg();
        String msg = tesseractService.trainBox(t);
        retMsg.success(msg);
        return ResponseEntity.ok(retMsg);
    }

    @RequestMapping("/testTessFile")
    public ResponseEntity testTessFile(@RequestParam("fileName") MultipartFile file, HttpServletRequest request) {
        RetMsg retMsg = new RetMsg();
        String lang = request.getParameter("lang");
        String msg = tesseractService.testPng(file, lang);
        if (StringUtils.isEmpty(msg)) {
            retMsg.fail();
        } else {
            retMsg.setData(msg);
        }
        return ResponseEntity.ok(retMsg);
    }
}
