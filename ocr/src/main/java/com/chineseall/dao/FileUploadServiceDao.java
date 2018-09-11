package com.chineseall.dao;

import com.chineseall.entity.UploadFileInfo;
import com.chineseall.entity.UploadPngTifInfo;
import org.springframework.stereotype.Repository;

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
}
