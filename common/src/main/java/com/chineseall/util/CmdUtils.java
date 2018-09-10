package com.chineseall.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 16:05.
 */
public class CmdUtils {

    private static Logger logger = LoggerFactory.getLogger(CmdUtils.class);

    public static void runCmdCommand(List<String> commandList, String cmdDesc) {
        logger.info("********Start {}********", cmdDesc);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commandList);
            processBuilder.redirectErrorStream(true);
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

    }
}
