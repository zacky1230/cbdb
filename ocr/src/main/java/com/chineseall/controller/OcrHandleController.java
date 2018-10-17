package com.chineseall.controller;

import com.alibaba.fastjson.JSONObject;
import com.chineseall.service.FileUploadService;
import com.chineseall.service.OcrHandleService;
import com.chineseall.util.MessageCode;
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
    protected FileUploadService fileUploadService;

    @Autowired
    private OcrHandleService ocrHandleService;

    /**
     * 上传图片带选择识别方式
     *
     * @param file
     * @param request
     * @param type
     * @return
     */
    @RequestMapping(value = "/ocr/upload/{type}")
    @ResponseBody
    public ResponseEntity imageUpload(@RequestParam("imgName") MultipartFile file, HttpServletRequest request,
                                      @PathVariable String type) {
        RetMsg retMsg = new RetMsg();

        if (file.getContentType().indexOf("image") == -1) {
            retMsg.fail(MessageCode.ImageFormatError.getDescription());
            return ResponseEntity.ok(retMsg);
        }

        Map<String, Object> map = fileUploadService.saveOcrImage(file, type);

        if (MessageCode.ImageUploadFail.getCode() == (int) map.get("code")) {
            retMsg.fail(MessageCode.ImageUploadFail.getDescription());
            return ResponseEntity.ok(retMsg);
        }
        String page = request.getParameter("page");
        map.put("page", page);
        map.put("type", request.getParameter("type"));

        retMsg = ocrHandleService.ocrImageHandle(map);

        return ResponseEntity.ok(retMsg);
    }


    /**
     * @return
     */
    @RequestMapping(value = "/ocr/image/{type}")
    @ResponseBody
    public ResponseEntity imageRecognition(@RequestBody Map<String, Object> map) {
        RetMsg retMsg = ocrHandleService.imageRecognition(map);
        return ResponseEntity.ok(retMsg);
    }
}
