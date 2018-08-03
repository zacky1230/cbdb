package com.chineseall.service.Impl;

import com.chineseall.dao.FileUploadServiceDao;
import com.chineseall.entity.UploadFileInfo;
import com.chineseall.service.BaiduAiService;
import com.chineseall.util.BaiduApiUtil;
import com.chineseall.util.RetMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 16:44.
 */
@Service("baiduAiServiceImpl")
public class BaiduAiServiceImpl implements BaiduAiService {

    @Autowired
    private FileUploadServiceDao fileUploadServiceDao;

    @Override
    public RetMsg getContext(int id) {
        UploadFileInfo uploadFileInfo = fileUploadServiceDao.queryById(id);
        if (uploadFileInfo != null) {
            String filePath = uploadFileInfo.getFileUploadPath();
            String accessToken = BaiduApiUtil.accessToken();
            String result = BaiduApiUtil.getImageContext(filePath, accessToken);
            System.out.println(result);
        }
        return null;
    }
}
