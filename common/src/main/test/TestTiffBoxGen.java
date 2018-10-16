import com.chineseall.util.FileUtil;
import com.chineseall.util.TiffBoxGenerator;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 14:00.
 */
public class TestTiffBoxGen {
    private static String context = FileUtil.readToString("/Users/zacky/Desktop/kangxi/kangxi.txt");
    static Font fontGen = new Font("TypeLand KhangXi Dict Demo", Font.PLAIN, 36);
    private static int height = 3000;
    private static int width = 3300;

    private static String outputDirectory = "/Users/zacky/Desktop/kangxi/";
    private static String fileName = "typelandkhangxidictdemo.exp0";
    private static float tracking = 0.0f;
    private static int leading = 12;
    private static int noise = 0;
    private static boolean antiAliasing = true;

    public static void main(String[] args) throws IOException {
        TiffBoxGenerator generator = new TiffBoxGenerator(context, fontGen, width, height);

        generator.setOutputFolder(new File(outputDirectory));
        generator.setFileName(fileName);
        generator.setTracking(tracking);
        generator.setLeading(leading);
        generator.setNoiseAmount(noise);
        generator.setAntiAliasing(antiAliasing);
        generator.create();
    }


}
