package com.chineseall.controller;

import com.chineseall.service.BaiduAiService;
import com.chineseall.util.RetMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 16:40.
 */
@Controller
public class BaiduAiController {

    @Autowired
    private BaiduAiService baiduAiService;

    @RequestMapping(value = "api/{id}", method = RequestMethod.GET)
    public ResponseEntity getContext(@PathVariable int id) {
        RetMsg retMsg;
        retMsg = baiduAiService.getContext(id);
        return ResponseEntity.ok(retMsg);
    }

    @RequestMapping(value = "api/split", method = RequestMethod.POST)
    public ResponseEntity getSplitContext(@RequestBody Map map){
        RetMsg retMsg;
        retMsg = baiduAiService.getSplitContext(map);
        return ResponseEntity.ok(retMsg);
    }
}
