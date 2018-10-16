package com.chineseall.service.impl;

import com.chineseall.dao.FileUploadServiceDao;
import com.chineseall.entity.UploadFileInfo;
import com.chineseall.entity.UploadPngTifInfo;
import com.chineseall.service.FileUploadService;
import com.chineseall.util.*;
import org.im4java.core.IM4JavaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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


    @Override
    public String saveOcrImage(MultipartFile file, String type) {
        String fileName = file.getOriginalFilename();

        String todayString = TimeUtil.getTodayToString();
        String extension = fileName.split("\\.")[1];
        String saveFileName = GenUuid.getUUID32() + "." + extension;
        if ("1".equals(type)) {
            todayString = todayString + File.separator + "1";
        } else if ("2".equals(type)) {
            todayString = todayString + File.separator + "2";
        }

        if (saveFile(file, saveFileName, todayString, fileName)) {
            return fileUploadPath + File.separator + todayString + File.separator + saveFileName;
        } else {
            return "fail";
        }
    }

    @Override
    public RetMsg saveSingleFile(MultipartFile file) {


        String todayString = TimeUtil.getTodayToString();

        String fileName = file.getOriginalFilename();

        String suffix = fileName.split("\\.")[1];

        String saveFileName = GenUuid.getUUID32() + "." + suffix;

        if (saveFile(file, saveFileName, todayString, fileName)) {
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

            String saveFileName = GenUuid.getUUID32() + "." + suffix;

            if (saveFile(file, saveFileName, todayString, fileName)) {
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


    public boolean saveFile(MultipartFile file, String saveFileName, String todayString, String fileName) {
        if (file.isEmpty()) {
            return false;
        }
        int size = (int) file.getSize();

        File dest = new File(fileUploadPath + File.separator + todayString + File.separator + saveFileName);
        /**
         *  判断文件父目录是否存在
         */
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        /**
         * 保存文件
         */
        try {
            file.transferTo(dest);
            UploadFileInfo uploadFileInfo = new UploadFileInfo();
            uploadFileInfo.setFileName(fileName);
            uploadFileInfo.setFileSaveName(saveFileName);
            uploadFileInfo.setFileUploadPath(dest.getPath());
            uploadFileInfo.setFileSize(size);
            fileUploadServiceDao.insert(uploadFileInfo);
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
