package com.chineseall.controller;

import com.chineseall.service.BaiduAiService;
import com.chineseall.service.FileUploadService;
import com.chineseall.util.RetMsg;
import com.chineseall.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 09:54.
 */

@Controller
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private BaiduAiService baiduAiService;

    private Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final ResourceLoader resourceLoader;

    @Autowired
    public FileUploadController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 实现单文件上传
     */
    @RequestMapping("fileUpload")
    @ResponseBody
    public ResponseEntity fileUpload(@RequestParam("fileName") MultipartFile file, HttpServletRequest request, Model model) {
        RetMsg retMsg = fileUploadService.saveSingleFile(file);
        String filePath = retMsg.getData().toString();
        Map<String, Object> map = new HashMap();
        map.put("filePath", filePath);
        map.put("xIndex", (request.getParameter("left")));
        map.put("yIndex", (request.getParameter("top")));
        map.put("row", 1);
        map.put("column", (request.getParameter("column")));
        map.put("gapStart", (request.getParameter("gapStart")));
        map.put("gapEnd", (request.getParameter("gapEnd")));
        map.put("rowsRight", 1);
        map.put("columnRight", (request.getParameter("columnRight")));
        map.put("yIndexRigth", (request.getParameter("gapEndY")));
        map.put("height", (request.getParameter("height")));
        retMsg = baiduAiService.getSplitContext(map);
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

    @RequestMapping("getPicture/{fileName}")
    public ResponseEntity getPicture(@PathVariable String fileName) throws IOException {
        if (StringUtils.isEmpty(fileName)) {
            fileName = "";
        } else {
            fileName = fileUploadService.getRealFilePath(fileName);
        }
        Path file = Paths.get(fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok(resourceLoader.getResource("file:" + file.toString()));
    }


}
