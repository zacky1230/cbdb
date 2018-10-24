package com.chineseall.util;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 17:39.
 */
public enum MessageCode {
    /**
     * image upload success code 1001
     */
    ImageUploadSuccess("图片上传成功！", 1001),

    /**
     * image recognition success code 1002
     */
    ImageRecognitionSuccess("图片识别成功！", 1002),

    /**
     * image load success code 1003
     */
    ImageLoadSuccess("图片加载成功！", 1003),

    /**
     * image info save success code 1004
     */
    ImageInfoSaveSuccess("图片内容保存成功！", 1004),

    /**
     * query database success code 1005
     */
    QuerySuccess("查询成功！", 1005),

    /**
     * image upload fail code 3001
     */
    ImageUploadFail("图片上传失败！", 3001),

    /**
     * uploaded image format error code 3002
     */
    ImageFormatError("图片格式错误！", 3002),

    /**
     * image to binary fail code 3003
     */
    Image2BinaryFail("图片二值化失败！", 3003),

    /**
     * image not found code 3004
     */
    ImageNotFound("图片未找到！", 3004),

    /**
     * image recognition fail code 3005
     */
    ImageRecognitionFail("图片识别失败！", 3005),

    /**
     * image info save fail code 3006
     */
    ImageInfoSaveFail("图片信息保存失败！", 3006),

    /**
     * param is error fail code 3007
     */
    ParamIsError("参数有误！", 3007);


    private String description;
    private int code;

    MessageCode(String description, int code) {
        this.description = description;
        this.code = code;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
