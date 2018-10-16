package com.chineseall.controller;

import com.chineseall.service.FileUploadService;
import com.chineseall.service.OcrHandleService;
import com.chineseall.util.RetMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
            retMsg.fail("error file type!");
            return ResponseEntity.ok(retMsg);
        }

        String realPath = fileUploadService.saveOcrImage(file, type);

        Map<String, Object> map = new HashMap();
        String page = request.getParameter("page");
        map.put("page", page);
        map.put("column", request.getParameter("column"));
        map.put("type", request.getParameter("type"));

        int[] data = ocrHandleService.ocrImageHandle(realPath, page);
        retMsg.setData(data);

        return ResponseEntity.ok(retMsg);
    }
}
