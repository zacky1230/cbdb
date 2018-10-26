package com.chineseall.controller;

import com.alibaba.fastjson.JSONArray;
import com.chineseall.entity.UploadFileContext;
import com.chineseall.service.OcrHandleService;
import com.chineseall.util.MessageCode;
import com.chineseall.util.RetMsg;
import com.chineseall.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

        double width = Double.parseDouble(request.getParameter("width"));
        double height = Double.parseDouble(request.getParameter("height"));
        RetMsg retMsg = ocrHandleService.imageUpload(file, type, width, height);
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

    /**
     * 保存图片
     */
    @RequestMapping(value = "/ocr/save/{imageId}")
    @ResponseBody
    public ResponseEntity saveImageInfo(@PathVariable String imageId, @RequestBody Map<String, Object> map) {
        UploadFileContext info = new UploadFileContext();
        String context = (String) map.get("context");
        LinkedHashMap<String, Object> coordinates = (LinkedHashMap) map.get("coordinate");
        String coordinate = JSONArray.toJSONString(coordinates);
        RetMsg retMsg = new RetMsg();
        if (StringUtils.isEmpty(context) || StringUtils.isEmpty(coordinate.toString()) || StringUtils.isEmpty(imageId)) {
            retMsg.setMsg(MessageCode.ParamIsError.getDescription());
            retMsg.setCode(MessageCode.ParamIsError.getCode());
            return ResponseEntity.ok(retMsg);
        } else {
            info.setFileId(imageId);
            info.setContext(context);
            info.setCoordinate(coordinate);
        }
        retMsg = ocrHandleService.saveImageInfo(imageId, info);
        return ResponseEntity.ok(retMsg);
    }


    @RequestMapping(value = "/ocr/query/{id}")
    @ResponseBody
    public ResponseEntity queryImageInfo(@PathVariable int id) {
        RetMsg retMsg = ocrHandleService.queryImageInfo(id);
        return ResponseEntity.ok(retMsg);
    }

    /**
     * 测试版本
     *
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/ocr/demo")
    @ResponseBody
    public ResponseEntity demo(@RequestParam("imgName") MultipartFile file, HttpServletRequest request) {
        String coordinates = request.getParameter("coordinate");
        double width = Double.parseDouble(request.getParameter("width"));
        double height = Double.parseDouble(request.getParameter("height"));
        Map<String, Object> imageInfo = new HashMap<>();
        imageInfo.put("coordinates", coordinates);
        imageInfo.put("width", width);
        imageInfo.put("height", height);
        RetMsg retMsg = ocrHandleService.imageDemo(file, imageInfo);
        return ResponseEntity.ok(retMsg);
    }
}
