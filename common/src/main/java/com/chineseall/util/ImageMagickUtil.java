package com.chineseall.util;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 10:11.
 */
public class ImageMagickUtil {
    private static Logger logger = LoggerFactory.getLogger(ImageMagickUtil.class);

    public static void main(String[] args) throws InterruptedException, IOException, IM4JavaException {
        pngToTif("/Users/zacky/Desktop/dian.png", "/Users/zacky/Desktop/dian2.png");
    }

    /**
     * Use convert api to make png to tif format.
     *
     * @param in
     * @param out
     * @throws InterruptedException
     * @throws IOException
     * @throws IM4JavaException
     */
    public static void pngToTif(String in, String out) throws InterruptedException, IOException, IM4JavaException {
        logger.info("pngToTif processing...");
        // create command
        ConvertCmd cmd = new ConvertCmd();
        // create the operation, add images and operators/options
        IMOperation op = new IMOperation();
        op.addImage(in);
        op.compose("none");
        op.monochrome();
        op.pointsize(2);
        op.depth(8);
        op.addImage(out);
        // execute the operation
        cmd.run(op);
        logger.info("pngToTif finished!");
    }
}
