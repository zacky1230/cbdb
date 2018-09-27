package com.chineseall.controller;

import com.chineseall.entity.UploadPngTifInfo;
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
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
     *
     * @param file
     * @param request
     * @param model
     * @return
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
     *
     * @param request
     * @return
     */
    @RequestMapping("multifileUpload")
    @ResponseBody
    public ResponseEntity multifileUpload(HttpServletRequest request) {

        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("fileName");

        RetMsg retMsg = fileUploadService.saveMultiFile(files);

        return ResponseEntity.ok(retMsg);
    }

    /**
     * 实现上传多文件
     *
     * @param request
     * @return
     */
    @RequestMapping("trainTessFile")
    public String trainTessFile(HttpServletRequest request, Model model) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("fileName");
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart(request);
        String lang = multipartRequest.getParameter("lang");
        String fontFamily = multipartRequest.getParameter("fontFamily");
        UploadPngTifInfo info = fileUploadService.saveTrainTessFile(file, lang, fontFamily);
        model.addAttribute("info", info);
        return "tess";
    }

    /**
     * 图片预览
     *
     * @param fileName
     * @return
     * @throws IOException
     */
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

    /**
     * 显示图片
     *
     * @param timeStamp
     * @return
     * @throws IOException
     */
    @RequestMapping("showTifPicture/{timeStamp}")
    public ResponseEntity showTifPicture(@PathVariable String timeStamp) throws IOException {
        if (StringUtils.isNotEmpty(timeStamp)) {
            String fileName = fileUploadService.getTifFilePathByTimeStamp(timeStamp);
            Path file = Paths.get(fileName);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return ResponseEntity.ok(resourceLoader.getResource("file:" + file.toString()));
        }
        return ResponseEntity.ok(resourceLoader.getResource("file:" + null));
    }


    /**
     * @param timeStamp
     * @return
     */
    @RequestMapping(value = "/image/get/{timeStamp}")
    @ResponseBody
    public String getImage(@PathVariable String timeStamp) {
        InputStream in;
        byte[] data = null;
        try {
            String fileName = fileUploadService.getTifFilePathByTimeStamp(timeStamp);
            in = new FileInputStream(fileName);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
}
