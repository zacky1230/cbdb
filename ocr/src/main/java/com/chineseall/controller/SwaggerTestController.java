package com.chineseall.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 18:27.
 */
public class SwaggerTestController {
    @Autowired
    private TesseractController tesseractController;

    private static final Logger logger = LoggerFactory.getLogger(SwaggerTestController.class);


    @ApiOperation(value = "", notes = "")
    @ApiImplicitParam(name = "id", value = "", paramType = "path", required = true, dataType = "Integer")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getStudent(@PathVariable String id) {
        logger.info("Starting training box!");
        return tesseractController.trainBox(id);
    }
}
