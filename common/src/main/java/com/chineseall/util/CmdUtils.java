package com.chineseall.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 16:05.
 */
public class CmdUtils {

    private static Logger logger = LoggerFactory.getLogger(CmdUtils.class);

    public static void runCmdCommand(List<String> commandList, String cmdDesc, String fileDir) {
        logger.info("********Start {}********", cmdDesc);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commandList);
            processBuilder.redirectErrorStream(true);
            processBuilder.directory(new File(fileDir));
            Process p = processBuilder.start();
            InputStream is = p.getInputStream();
            BufferedReader bs = new BufferedReader(new InputStreamReader(is));

            p.waitFor();

            String line;
            while ((line = bs.readLine()) != null) {
                logger.info(line);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        logger.info("********End {}********", cmdDesc);
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        String fileDir = "/Users/zacky/Desktop/tess";
        // box train
        String boxTraincmd = "tesseract imageFile boxFile -psm 10 nobatch box.train";
        String cmdStr = boxTraincmd.replace("imageFile", fileDir + File.separator + "chi.SimSun.tif").replace("boxFile", fileDir + File.separator +
                "chi.SimSun");

        //unicharsetExtractor
        /*String cmdunicharset_extractor = "unicharset_extractor boxFile";
        String cmdStr = cmdunicharset_extractor.replace("boxFile","chi.SimSun.box");*/

        //generatorFontProperties
        /*int size = 2;
        StringBuffer cmdStr = new StringBuffer();
        cmdStr.append("echo '");
        for (int i = 0; i < size; i++) {
            cmdStr.append("SimSun 0 0 0 0 0\n");
        }
        cmdStr.append("'> font_properties");*/


       /* String cmdshapeclustering = "shapeclustering -F font_properties -U unicharset trFile"; //
        String cmdStr = cmdshapeclustering.replace("trFile", "chi.SimSun.tr");*/

        /*String cmdmftraining = "mftraining -F font_properties -U unicharset -O %s.unicharset trFile";
        String cmdStr = cmdmftraining.replace("trFile", "chi.SimSun.tr").replace("%s","chi.SimSun");*/

        /*String cmdcntraining = "cntraining trFile";
        String cmdStr = cmdcntraining.replace("trFile","chi.SimSun.tr");*/

        /*String cmdStr = "mv inttemp " + "chi.SimSun" + ".inttemp";*/

        list.addAll(Arrays.asList(cmdStr.split("\\s+")));
        CmdUtils.runCmdCommand(list, "change directory", fileDir);
    }
}
