package com.chineseall.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 15:18.
 */
public interface TesseractService {
    boolean saveBoxToFile(JSONObject jsonParam) throws IOException;

    String trainBox(String t);

    String testPng(MultipartFile file, String lang);
}
