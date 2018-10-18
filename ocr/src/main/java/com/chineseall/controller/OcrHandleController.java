package com.chineseall.controller;

import com.chineseall.service.OcrHandleService;
import com.chineseall.util.RetMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 13:47.
 */
@Controller
public class OcrHandleController {

    @Autowired
    private OcrHandleService ocrHandleService;

    /**
     * 上传图片带选择识别方式
     */
    @RequestMapping(value = "/ocr/upload/{type}")
    @ResponseBody
    public ResponseEntity imageUpload(@RequestParam("imgName") MultipartFile file, HttpServletRequest request,
                                      @PathVariable String type) {

        String page = request.getParameter("page");
        RetMsg retMsg = ocrHandleService.imageUpload(file, type, page);
        return ResponseEntity.ok(retMsg);
    }


    /**
     * 图片识别
     */
    @RequestMapping(value = "/ocr/image/{type}")
    @ResponseBody
    public ResponseEntity imageRecognition(@RequestBody Map<String, Object> map) {
        RetMsg retMsg = ocrHandleService.imageRecognition(map);
        return ResponseEntity.ok(retMsg);
    }


    /**
     * 获取处理好的图片
     */
    @RequestMapping(value = "/ocr/handler/{imageId}")
    @ResponseBody
    public ResponseEntity getHandlerImage(@PathVariable String imageId) {
        RetMsg retMsg = ocrHandleService.getHandlerImage(imageId);
        return ResponseEntity.ok(retMsg);
    }

}
