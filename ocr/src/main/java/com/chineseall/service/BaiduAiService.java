package com.chineseall.service;

import com.chineseall.util.RetMsg;

import java.util.Map;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 16:44.
 */
public interface BaiduAiService {
    RetMsg getContext(int id);

    RetMsg getSplitContext(Map map);
}
