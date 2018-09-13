package com.chineseall.service;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 15:18.
 */
public interface TesseractService {
    void saveBoxToFile(JSONObject jsonParam) throws IOException;

    void trainBox(String t);
}
