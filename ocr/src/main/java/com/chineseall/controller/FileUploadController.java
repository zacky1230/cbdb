package com.chineseall.controller;

import com.chineseall.service.FileUploadService;
import com.chineseall.util.ResultUtil;
import com.chineseall.util.RetMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 09:54.
 */

@Controller
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    private Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @RequestMapping(value = "toFilePage", method = RequestMethod.GET)
    public String toFilePage() {
        logger.info("show file upload page");
        return "/file";
    }

    @RequestMapping(value = "toMultiFilePage", method = RequestMethod.GET)
    public String toMultiFilePage() {
        logger.info("show file upload page");
        return "/multifile";
    }

    /**
     * 实现单文件上传
     */
    @RequestMapping("fileUpload")
    @ResponseBody
    public ResponseEntity fileUpload(@RequestParam("fileName") MultipartFile file) {

        RetMsg retMsg = fileUploadService.saveSingleFile(file);

        return ResponseEntity.ok(retMsg);
    }

    /**
     * 实现多文件上传
     */
    @RequestMapping("multifileUpload")
    @ResponseBody
    public ResponseEntity multifileUpload(HttpServletRequest request) {

        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("fileName");

        RetMsg retMsg = fileUploadService.saveMultiFile(files);

        return ResponseEntity.ok(retMsg);
    }
}
