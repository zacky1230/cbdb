package com.chineseall.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chineseall.service.TesseractService;
import com.chineseall.util.GenUuid;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 15:18.
 */
@Service("tesseractServiceImpl")
public class TesseractServiceImpl implements TesseractService {

    @Override
    public void saveBoxToFile(JSONArray jsonParam) throws IOException {
        String filePath = "/Users/zacky/Desktop/" + GenUuid.getUUID32() + ".box";
        FileWriter fileWriter;
        BufferedWriter writer = null;

        fileWriter = new FileWriter(filePath);

        writer = new BufferedWriter(fileWriter);
        if (jsonParam != null && jsonParam.size() > 0) {
            for (int i = 0; i < jsonParam.size(); i++) {
                JSONObject jsonObject = jsonParam.getJSONObject(i);
                StringBuffer sb = new StringBuffer();
                sb.append(jsonObject.get("char"));
                sb.append(" ");
                sb.append(jsonObject.get("x"));
                sb.append(" ");
                sb.append(jsonObject.get("y"));
                sb.append(" ");
                sb.append(jsonObject.get("w"));
                sb.append(" ");
                sb.append(jsonObject.get("h"));
                sb.append(" ");
                sb.append(0);
                writer.write(sb.toString());
                writer.newLine();
            }
        }

        writer.flush();
        writer.close();

    }
}

