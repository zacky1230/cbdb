package com.chineseall.dao;

import com.chineseall.entity.UploadFileContext;
import com.chineseall.entity.UploadFileInfo;
import com.chineseall.entity.UploadPngTifInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 15:26.
 */
@Repository("fileUploadServiceDao")
public interface FileUploadServiceDao {
    Integer insert(UploadFileInfo uploadFileInfo);

    UploadFileInfo queryById(int id);

    String queryByFileSaveName(String fileName);

    Integer add(UploadPngTifInfo info);

    UploadPngTifInfo getTifFilePathByTimeStamp(String timeStamp);

    Integer updateBoxInfo(HashMap hashMap);

    Integer updateImageInfo(UploadFileContext info);

    UploadFileContext queryImageInfoById(int id);
}
