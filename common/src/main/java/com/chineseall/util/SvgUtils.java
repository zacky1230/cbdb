package com.chineseall.util;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 17:28.
 */
public class SvgUtils {

    public static void main(String[] args) throws IOException {
        XmlHelper xmlHelper = XmlHelper.of(new FileInputStream("/Users/zacky/Desktop/康熙字典体.svg"));
        NodeList list = xmlHelper.getNodeList("/svg/defs/font/glyph[starts-with(@glyph-name,'uni')]");

        System.out.println(list.getLength());

        FileWriter fw = null;
        BufferedWriter bw = null;
        String outPath = "/Users/zacky/Desktop/kangxitrainner/";

        int listSize = list.getLength();

        int size = 500;

        for (int i = 0; i <= listSize / size; i++) {
            List<String> stringBuffer = new ArrayList<>();


            if ((i + 1) * size > listSize) {
                for (int j = i * size; j < listSize; j++) {
                    Element e = (Element) list.item(j);
                    stringBuffer.add(e.getAttribute("unicode"));
                }
            } else {
                for (int j = i * size; j < (i + 1) * size; j++) {
                    Element e = (Element) list.item(j);
                    stringBuffer.add(e.getAttribute("unicode"));
                }
            }


            File f = new File(outPath + i + ".txt");
            try {
                if (!f.exists()) {
                    f.createNewFile();
                }
                fw = new FileWriter(f.getAbsoluteFile(), true);
                bw = new BufferedWriter(fw);
                for (int k = 0; k < stringBuffer.size(); k++) {
                    bw.write(stringBuffer.get(k));
                    if (k % 20 == 0 && k > 0) {
                        bw.newLine();
                    }
                }

                bw.flush();
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
