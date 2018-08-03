package com.chineseall.util;

import java.util.UUID;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 14:09.
 * 生成UUID
 */
public class GenUuid {
    public static String getUUID32() {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        return uuid;
    }
}
