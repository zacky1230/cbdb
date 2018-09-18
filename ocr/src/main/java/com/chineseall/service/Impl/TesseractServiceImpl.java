package com.chineseall.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chineseall.dao.FileUploadServiceDao;
import com.chineseall.entity.UploadPngTifInfo;
import com.chineseall.service.TesseractService;
import com.chineseall.util.FileUtil;
import com.chineseall.util.StringUtils;
import com.chineseall.util.TrainingProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 15:18.
 */
@Service("tesseractServiceImpl")
public class TesseractServiceImpl implements TesseractService {

    @Autowired
    private FileUploadServiceDao fileUploadServiceDao;

    @Autowired
    private TrainingProcess trainingProcess;

    @Override
    public void saveBoxToFile(JSONObject jsonParam) throws IOException {
        String t = jsonParam.getString("t");
        if (StringUtils.isEmpty(t)) {
            return;
        }

        UploadPngTifInfo info = fileUploadServiceDao.getTifFilePathByTimeStamp(t);

        String boxName = info.getLang() + "." + info.getFontFamily() + "." + t + ".box";

        String filePath = info.getUploadDirectory() + File.separator + boxName;
        FileWriter fileWriter;
        BufferedWriter writer;

        fileWriter = new FileWriter(filePath);

        writer = new BufferedWriter(fileWriter);
        JSONArray jsonArray = jsonParam.getJSONArray("data");
        if (jsonArray != null && jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
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
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("timeStamp", t);
        hashMap.put("boxName", boxName);
        fileUploadServiceDao.updateBoxInfo(hashMap);
    }

    /**
     * 根据时间戳来获取新增的文件信息
     *
     * @param t
     */
    @Override
    public void trainBox(String t) {
        if (StringUtils.isEmpty(t)) {
            return;
        }
        UploadPngTifInfo info = fileUploadServiceDao.getTifFilePathByTimeStamp(t);

        String lang = info.getLang();

        String parentDir = FileUtil.getParentDirectory(info.getUploadDirectory());

        String fileDir = parentDir + File.separator + info.getTimeStamp();

        String prefix = lang + "." + info.getFontFamily() + "." + info.getTimeStamp();

        trainingProcess.boxTrain(fileDir, info.getTifFileName(), prefix);

        String tempDir = FileUtil.moveFile(fileDir, prefix);

        List<String> boxFilesName = FileUtil.getFilesBySuffix(tempDir, ".box");

        trainingProcess.unicharsetExtractor(tempDir, boxFilesName);

        String[] fontFamilys = getFontFamily(boxFilesName);

        trainingProcess.generatorFontProperties(boxFilesName, tempDir, fontFamilys);

        List<String> trFilesName = FileUtil.getFilesBySuffix(tempDir, ".tr");

        trainingProcess.shapeclustering(trFilesName, tempDir);

        trainingProcess.mftraining(trFilesName, tempDir, lang);

        trainingProcess.cntraining(trFilesName, tempDir);

        trainingProcess.renameInttemp(tempDir, lang);

        trainingProcess.renameNormproto(tempDir, lang);

        trainingProcess.renamePffmtable(tempDir, lang);

        trainingProcess.renameShapetable(tempDir, lang);

        trainingProcess.combineTessdata(lang, tempDir);

        trainingProcess.mvTrainedData(lang, tempDir);
    }

    private String[] getFontFamily(List<String> boxFilesName) {
        int length = boxFilesName.size();
        String[] strings = new String[length];
        for (int i = 0; i < length; i++) {
            strings[i] = getFontFamily(boxFilesName.get(i));
        }
        return strings;
    }

    private String getFontFamily(String boxFileName) {
        return boxFileName.split("\\.")[1];
    }
}

