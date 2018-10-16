package com.chineseall.util;

import java.util.UUID;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 14:09.
 * 生成UUID
 */
public class GenUuid {
    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
