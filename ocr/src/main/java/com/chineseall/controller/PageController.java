package com.chineseall.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 10:08.
 */
@Controller
public class PageController {

    private Logger logger = LoggerFactory.getLogger(PageController.class);

    @RequestMapping(value = "toFilePage", method = RequestMethod.GET)
    public String toFilePage() {
        logger.info("show file upload page");
        return "/file";
    }

    @RequestMapping(value = "toMultiFilePage", method = RequestMethod.GET)
    public String toMultiFilePage() {
        logger.info("show file upload page");
        return "/multifile";
    }

    @RequestMapping(value = "toOcrPage", method = RequestMethod.GET)
    public String toOcrPage() {
        logger.info("show ocr page");
        return "/ocr";
    }

    @RequestMapping(value = "toTessPage", method = RequestMethod.GET)
    public String toTessPage() {
        logger.info("show tess page");
        return "/tess";
    }

    @RequestMapping(value = "toTessFilePage", method = RequestMethod.GET)
    public String toTessFilePage() {
        logger.info("show tessFile page");
        return "/tessFile";
    }
}
