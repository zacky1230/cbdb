package com.chineseall.util;

import java.beans.PropertyChangeSupport;
import java.io.File;
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


    private static final String cmdtess_train = "tesseract imageFile boxFile -psm 10 nobatch box.train";
    private static final String cmdunicharset_extractor = "unicharset_extractor boxFile"; // lang.fontname.exp0.box lang// .fontname.exp1.box ...
    private static final String cmdset_unicharset_properties = "set_unicharset_properties -U unicharset -O unicharset --script_dir=%s";
    private static final String cmdshapeclustering = "shapeclustering -F font_properties -U unicharset trFile"; //
    private static final String cmdmftraining = "mftraining -F font_properties -U unicharset -O %s.unicharset trFile"; // lang
    private static final String cmdcntraining = "cntraining trFile"; // lang.fontname.exp0.tr lang.fontname.exp1.tr
    private static final String cmdwordlist2dawg = "wordlist2dawg %2$s %1$s.frequent_words_list %1$s.freq-dawg %1$s.unicharset";
    private static final String cmdwordlist2dawg2 = "wordlist2dawg %2$s %1$s.words_list %1$s.word-dawg %1$s.unicharset";
    private static final String cmdpunc2dawg = "wordlist2dawg %2$s %1$s.punc %1$s.punc-dawg %1$s.unicharset";
    private static final String cmdnumber2dawg = "wordlist2dawg %2$s %1$s.numbers %1$s.number-dawg %1$s.unicharset";
    private static final String cmdbigrams2dawg = "wordlist2dawg %2$s %1$s.word.bigrams %1$s.bigram-dawg %1$s.unicharset";
    private static final String cmdcombine_tessdata = "combine_tessdata %s.";


    public TrainingProcess(){
    }


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
     * @param fileDir
     * @param tiffPath
     * @param boxPath
     */
    private void boxTrain(String fileDir, String tiffPath, String boxPath) {
        logger.info("Run Tesseract for Training");
        String cmdStr = cmdtess_train.replace("imageFile", tiffPath).replace("boxFile", boxPath);
        CmdUtils.runCmdCommand(getCommand(cmdStr), "Box Train", fileDir);
    }

    /**
     * Unicharset Extractor
     *
     * @param fileDir
     * @param boxFiles
     */
    private void unicharsetExtractor(String fileDir, List<String> boxFiles) {
        logger.info("Compute the Character Set");
        StringBuffer boxFile = new StringBuffer();
        for (String str : boxFiles) {
            boxFile.append(str + " ");
        }
        String cmdStr = cmdunicharset_extractor.replace("boxFile", boxFile.toString());
        CmdUtils.runCmdCommand(getCommand(cmdStr), "unicharset extractor", fileDir);
    }

    /**
     * SetUnicharsetProperties
     *
     * @param langDir
     */
    private void setUnicharsetProperties(String fileDir, String langDir) {
        String cmdStr = cmdset_unicharset_properties.replace("%s", langDir);
        CmdUtils.runCmdCommand(getCommand(cmdStr), "Box Train", fileDir);
    }

    /**
     * generatorFontProperties
     *
     * @param boxFiles
     * @param fileDir
     */
    private void generatorFontProperties(List<String> boxFiles, String fileDir, String[] fontFamily) {
        int size = boxFiles.size();
        FileUtil.writeToFile(fileDir + File.separator + "font_properties", fontFamily, boxFiles.size());
    }

    /**
     * shapeclustering
     *
     * @param trFile
     * @param fileDir
     */
    private void shapeclustering(String trFile, String fileDir) {
        String cmdStr = cmdshapeclustering.replace("trFile", trFile);
        CmdUtils.runCmdCommand(getCommand(cmdStr), "shapeclustering", fileDir);
    }

    /**
     * mftraining
     *
     * @param trFile
     * @param fileDir
     */
    private void mftraining(String trFile, String fileDir, String fontFamily) {
        String cmdStr = cmdmftraining.replace("trFile", trFile).replace("%s", fontFamily);
        CmdUtils.runCmdCommand(getCommand(cmdStr), "mftraining", fileDir);
    }

    /**
     * cntraining
     *
     * @param trFile
     * @param fileDir
     */
    private void cntraining(String trFile, String fileDir) {
        String cmdStr = cmdcntraining.replace("trFile", trFile);
        CmdUtils.runCmdCommand(getCommand(cmdStr), "cntraining", fileDir);
    }

    /**
     * rename inttemp
     *
     * @param fileDir
     * @param lang
     */
    private void renameInttemp(String fileDir, String lang) {
        String cmdStr = "mv inttemp " + lang + ".inttemp";
        CmdUtils.runCmdCommand(getCommand(cmdStr), "inttemp", fileDir);
    }

    /**
     * rename normproto
     *
     * @param fileDir
     * @param lang
     */
    private void renameNormproto(String fileDir, String lang) {
        String cmdStr = "mv normproto " + lang + ".normproto";
        CmdUtils.runCmdCommand(getCommand(cmdStr), "normproto", fileDir);
    }


    /**
     * rename pffmtable
     *
     * @param fileDir
     * @param lang
     */
    private void renamePffmtable(String fileDir, String lang) {
        String cmdStr = "mv pffmtable " + lang + ".pffmtable";
        CmdUtils.runCmdCommand(getCommand(cmdStr), "pffmtable", fileDir);
    }

    /**
     * rename shapetable
     *
     * @param fileDir
     * @param lang
     */
    private void renameShapetable(String fileDir, String lang) {
        String cmdStr = "mv shapetable " + lang + ".shapetable";
        CmdUtils.runCmdCommand(getCommand(cmdStr), "shapetable", fileDir);
    }

    /**
     * combine tessdata
     *
     * @param lang
     * @param fileDir
     */
    private void combineTessdata(String lang, String fileDir) {
        String cmdStr = "combine_tessdata " + lang + ".";
        CmdUtils.runCmdCommand(getCommand(cmdStr), "combine tessdata", fileDir);
    }

    /**
     * move trainneddata
     *
     * @param lang
     * @param fileDir
     * @param tessDataDir
     */
    private void mvTrainedData(String lang, String fileDir, String tessDataDir) {
        String cmdStr = "mv " + lang + ".traineddata " + tessDataDir;
        CmdUtils.runCmdCommand(getCommand(cmdStr), "mv traineddata", fileDir);
    }


    List<String> getCommand(String cmdStr) {
        List<String> cmd = new LinkedList<>(Arrays.asList(cmdStr.split("\\s+")));
        cmd.set(0, tessDir + "/" + cmd.get(0));
        return cmd;
    }

}
