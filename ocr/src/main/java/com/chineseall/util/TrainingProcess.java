package com.chineseall.util;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 11:16.
 */
public class TrainingProcess {

    private final static Logger logger = Logger.getLogger(TrainingProcess.class.getName());

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    ProcessBuilder pb = new ProcessBuilder();

    String tessDir;


    private static final String cmdtess_train = "tesseract imageFile boxFile box.train";

    private static final String cmdunicharset_extractor = "unicharset_extractor"; // lang.fontname.exp0.box lang.fontname.exp1.box ...
    private static final String cmdset_unicharset_properties = "set_unicharset_properties -U unicharset -O unicharset --script_dir=%s";
    private static final String cmdshapeclustering = "shapeclustering -F %s.font_properties -U unicharset"; // lang.fontname.exp0.tr lang.fontname.exp1.tr ...";
    private static final String cmdmftraining = "mftraining -F %1$s.font_properties -U unicharset -O %1$s.unicharset"; // lang.fontname.exp0.tr lang.fontname.exp1.tr ...";
    private static final String cmdcntraining = "cntraining"; // lang.fontname.exp0.tr lang.fontname.exp1.tr ...";
    private static final String cmdwordlist2dawg = "wordlist2dawg %2$s %1$s.frequent_words_list %1$s.freq-dawg %1$s.unicharset";
    private static final String cmdwordlist2dawg2 = "wordlist2dawg %2$s %1$s.words_list %1$s.word-dawg %1$s.unicharset";
    private static final String cmdpunc2dawg = "wordlist2dawg %2$s %1$s.punc %1$s.punc-dawg %1$s.unicharset";
    private static final String cmdnumber2dawg = "wordlist2dawg %2$s %1$s.numbers %1$s.number-dawg %1$s.unicharset";
    private static final String cmdbigrams2dawg = "wordlist2dawg %2$s %1$s.word.bigrams %1$s.bigram-dawg %1$s.unicharset";
    private static final String cmdcombine_tessdata = "combine_tessdata %s.";

    /**
     * Generate Box File
     *
     * @param inputPath
     * @param outPath
     * @throws IOException
     */
    private void makeBoxFile(String inputPath, String outPath) throws IOException {
        FileUtil.convertToTiff(inputPath, outPath);
    }

    /**
     * Box Train
     *
     * @param tiffPath
     * @param boxPath
     */
    private void boxTrain(String tiffPath, String boxPath) {
        logger.info("Run Tesseract for Training");
        String cmdStr = cmdtess_train.replace("imageFile", tiffPath).replace("boxFile", boxPath);
        CmdUtils.runCmdCommand(getCommand(cmdStr), "Box Train");
    }

    /**
     * Unicharset Extractor
     *
     * @param trPath
     */
    private void unicharsetExtractor(String trPath) {
        logger.info("Compute the Character Set");
        List<String> cmd = getCommand(cmdunicharset_extractor);
        cmd.add(trPath);
        CmdUtils.runCmdCommand(cmd, "unicharset extractor");
    }

    /**
     * SetUnicharsetProperties
     *
     * @param langDir
     */
    private void setUnicharsetProperties(String langDir) {
        String cmdStr = cmdset_unicharset_properties.replace("%s", langDir);
        CmdUtils.runCmdCommand(getCommand(cmdStr), "Box Train");
    }

    List<String> getCommand(String cmdStr) {
        List<String> cmd = new LinkedList<>(Arrays.asList(cmdStr.split("\\s+")));
        cmd.set(0, tessDir + "/" + cmd.get(0));
        return cmd;
    }

}
