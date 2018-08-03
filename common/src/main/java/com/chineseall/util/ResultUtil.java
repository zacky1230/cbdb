package com.chineseall.util;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 09:46.
 */
public class ResultUtil {
    /**
     * 请求成功返回
     */
    public static RetMsg success(Object object, Integer code, String resultMsg) {
        RetMsg msg = new RetMsg();
        msg.setCode(code);
        msg.setMsg(resultMsg);
        msg.setData(object);
        return msg;
    }

    public static RetMsg success(String resultMsg) {
        return success(null, 200, resultMsg);
    }

    public static RetMsg success(String resultMsg, Object object) {
        return success(object, 200, resultMsg);
    }

    public static RetMsg error(Integer code, String resultMsg) {
        RetMsg msg = new RetMsg();
        msg.setCode(code);
        msg.setMsg(resultMsg);
        return msg;
    }
}
