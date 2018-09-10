package com.chineseall.Test;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 10:11.
 */
public class TestTesseract {
    public static void main(String[] args) throws TesseractException {
        ITesseract iTesseract = new Tesseract();
        File file = new File("/Users/zacky/Desktop/eng.png");
        iTesseract.setDatapath("/Users/zacky/Desktop/work/tess4j/src/main/resources/tessdata");
        String result = iTesseract.doOCR(file);
        System.out.println(result);
    }
}
