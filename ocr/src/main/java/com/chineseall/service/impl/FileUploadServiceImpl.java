package com.chineseall.service.impl;

import com.chineseall.dao.FileUploadServiceDao;
import com.chineseall.entity.UploadFileInfo;
import com.chineseall.entity.UploadPngTifInfo;
import com.chineseall.service.FileUploadService;
import com.chineseall.util.*;
import org.im4java.core.IM4JavaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 12:08.
 */
@Service("fileUploadServiceImpl")
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.upload.path}")
    private String fileUploadPath;

    @Autowired
    private FileUploadServiceDao fileUploadServiceDao;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public Map<String, Object> saveOcrImage(MultipartFile file, String type, double width, double height) {
        Map<String, Object> retMap = new HashMap<>();
        String fileName = file.getOriginalFilename();

        String todayString = TimeUtil.getTodayToString();
        String extension = fileName.split("\\.")[1];
        String uuid = GenUuid.getUUID32();
        String saveFileName = uuid + "." + extension;
        if ("1".equals(type)) {
            todayString = todayString + File.separator + "1";
        } else if ("2".equals(type)) {
            todayString = todayString + File.separator + "2";
        }

        String fileDir = fileUploadPath + File.separator + todayString;
        String handlePath = fileUploadPath + File.separator + todayString + File.separator + uuid + "_handle.png";
        if (saveImage(file, saveFileName, fileName, uuid, fileDir, width, height)) {
            retMap.put("msg", MessageCode.ImageUploadSuccess.getDescription());
            retMap.put("code", MessageCode.ImageUploadSuccess.getCode());
            retMap.put("filePath", handlePath);
            retMap.put("imageId", uuid);
            return retMap;
        } else {
            retMap.put("msg", MessageCode.ImageUploadFail.getDescription());
            retMap.put("code", MessageCode.ImageUploadFail.getCode());
            return retMap;
        }
    }

    @Override
    public RetMsg saveSingleFile(MultipartFile file) {


        String todayString = TimeUtil.getTodayToString();

        String fileName = file.getOriginalFilename();

        String suffix = fileName.split("\\.")[1];

        String uuid = GenUuid.getUUID32();

        String saveFileName = uuid + "." + suffix;

        if (saveFile(file, saveFileName, todayString, fileName, uuid)) {
            String filePath = fileUploadPath + File.separator + todayString + File.separator + saveFileName;
            return ResultUtil.success("upload success", filePath);
        } else {
            return ResultUtil.success("upload fail");
        }
    }

    @Override
    public RetMsg saveMultiFile(List<MultipartFile> files) {
        if (files.isEmpty()) {
            return ResultUtil.success("upload fail");
        }
        List<String> pathList = new ArrayList<>();
        for (MultipartFile file : files) {
            String todayString = TimeUtil.getTodayToString();

            String fileName = file.getOriginalFilename();

            String suffix = fileName.split("\\.")[1];

            String uuid = GenUuid.getUUID32();
            String saveFileName = uuid + "." + suffix;

            if (saveFile(file, saveFileName, todayString, fileName, uuid)) {
                pathList.add(saveFileName);
            }
        }
        if (pathList.size() == 0) {
            return ResultUtil.success("upload fail");
        }
        if (files.size() == pathList.size()) {
            return ResultUtil.success("upload success", pathList);
        } else {
            return ResultUtil.success("some upload success", pathList);
        }
    }

    @Override
    public String getRealFilePath(String fileName) {
        return fileUploadServiceDao.queryByFileSaveName(fileName);
    }

    @Override
    public UploadPngTifInfo saveTrainTessFile(MultipartFile file, String lang, String fontFamily) {
        if (file.isEmpty()) {
            return null;
        }
        long currentTime = TimeUtil.getCurrentTimeStamp();


        String pngFileName = lang + "." + fontFamily + "." + currentTime + ".png";
        String tifFileName = lang + "." + fontFamily + "." + currentTime + ".tif";

        saveFile(file, pngFileName, currentTime);

        try {
            ImageMagickUtil.pngToTif(fileUploadPath + File.separator + currentTime + File.separator + pngFileName,
                    fileUploadPath + File.separator + currentTime + File.separator + tifFileName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IM4JavaException e) {
            e.printStackTrace();
        }

        UploadPngTifInfo info = new UploadPngTifInfo();

        info.setUploadDirectory(fileUploadPath + File.separator + currentTime);
        info.setFontFamily(fontFamily);
        info.setLang(lang);
        info.setTimeStamp(currentTime);
        info.setPngFileName(pngFileName);
        info.setTifFileName(tifFileName);
        fileUploadServiceDao.add(info);

        return info;
    }

    @Override
    public String getTifFilePathByTimeStamp(String timeStamp) {
        UploadPngTifInfo info = fileUploadServiceDao.getTifFilePathByTimeStamp(timeStamp);
        if (info != null) {
            return info.getUploadDirectory() + File.separator + info.getPngFileName();
        }
        return null;
    }

    private boolean saveImage(MultipartFile file, String saveFileName, String fileName, String imageId, String fileDir,
                              double width, double height) {
        String originalPath = fileDir + File.separator + saveFileName;
        String handlePath = fileDir + File.separator + imageId + "_handle.png";
        return saveFile(file, saveFileName, fileName, imageId, width, originalPath, handlePath) && setValueToRedis
                (imageId, handlePath);
    }

    private boolean saveFile(MultipartFile file, String saveFileName, String fileName, String imageId, double width,
                             String originalPath, String handlePath) {
        if (file.isEmpty()) {
            return false;
        }
        int size = (int) file.getSize();

        File dest = new File(originalPath);

        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            file.transferTo(dest);

            Integer imageWidth = Integer.parseInt(new java.text.DecimalFormat("0").format(width));

            ImageMagickUtil.imageZoomInPng(originalPath, handlePath, imageWidth);

            saveFileInfoToDB(fileName, saveFileName, handlePath, size, imageId);

            return true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (IM4JavaException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void saveFileInfoToDB(String fileName, String saveFileName, String fileUploadPath, int size, String imageId) {
        UploadFileInfo uploadFileInfo = new UploadFileInfo();
        uploadFileInfo.setFileName(fileName);
        uploadFileInfo.setFileSaveName(saveFileName);
        uploadFileInfo.setFileUploadPath(fileUploadPath);
        uploadFileInfo.setFileSize(size);
        uploadFileInfo.setFileId(imageId);
        fileUploadServiceDao.insert(uploadFileInfo);
    }

    private boolean setValueToRedis(String key, String value) {
        value = value.replace("handle", "handle_result");
        redisTemplate.opsForValue().set(key, value, 12, TimeUnit.HOURS);
        if (value.equals(redisTemplate.opsForValue().get(key))) {
            return true;
        }
        return false;
    }

    private boolean saveFile(MultipartFile file, String saveFileName, long currentTime) {
        if (file.isEmpty()) {
            return false;
        }
        /**int size = (int) file.getSize();**/

        File dest = new File(fileUploadPath + File.separator + currentTime + File.separator + saveFileName);

        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }

        try {
            file.transferTo(dest);
            return true;

        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean saveFile(MultipartFile file, String saveFileName, String todayString, String fileName, String
            fileId) {
        if (file.isEmpty()) {
            return false;
        }
        int size = (int) file.getSize();

        File dest = new File(fileUploadPath + File.separator + todayString + File.separator + saveFileName);

        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            file.transferTo(dest);
            saveFileInfoToDB(fileName, saveFileName, dest.getPath(), size, fileId);
            return true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
