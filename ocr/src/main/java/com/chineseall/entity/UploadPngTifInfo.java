package com.chineseall.entity;

import java.util.Date;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 15:42.
 */
public class UploadPngTifInfo {
    private Integer id;
    private String uploadDirectory;
    private String pngFileName;
    private String tifFileName;
    private String boxName;
    private String fontFamily;
    private String lang;
    private long timeStamp;
    private Date createTime;
    private Date updateTime;
    private int deleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUploadDirectory() {
        return uploadDirectory;
    }

    public void setUploadDirectory(String uploadDirectory) {
        this.uploadDirectory = uploadDirectory;
    }

    public String getPngFileName() {
        return pngFileName;
    }

    public void setPngFileName(String pngFileName) {
        this.pngFileName = pngFileName;
    }

    public String getTifFileName() {
        return tifFileName;
    }

    public void setTifFileName(String tifFileName) {
        this.tifFileName = tifFileName;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }
}
