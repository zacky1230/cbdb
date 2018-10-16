package com.chineseall.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.alibaba.fastjson.util.IOUtils.close;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 17:35.
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     *
     * @param fileName 文件的名
     */
    public static byte[] readFileByBytes(String fileName) {
        InputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            in = new FileInputStream(fileName);
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }
        return out.toByteArray();
    }

    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     *
     * @param fileName 文件的名
     */
    public static void readFileByByte(String fileName) {
        File file = new File(fileName);
        InputStream in = null;
        try {
            logger.info("以字节为单位读取文件内容，一次读多个字节：");
            byte[] tempbytes = new byte[100];
            int byteread = 0;
            in = new FileInputStream(file);
            showAvailableBytes(in);
            while ((byteread = in.read(tempbytes)) != -1) {
                System.out.write(tempbytes, 0, byteread);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     *
     * @param fileName 文件名
     */
    public static void readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        try {
            logger.info("以字符为单位读取文件内容，一次读多个字节：");
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(file));
            while ((charread = reader.read(tempchars)) != -1) {
                if ((charread == tempchars.length)
                        && (tempchars[tempchars.length - 1] != 'r')) {
                    System.out.print(tempchars);
                } else {
                    for (int i = 0; i < charread; i++) {
                        if (tempchars[i] == 'r') {
                            continue;
                        } else {
                            System.out.print(tempchars[i]);
                        }
                    }
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     *
     * @param fileName 文件名
     */
    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            logger.info("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                logger.info(String.format("The line %s : ", line, tempString));
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 随机读取文件内容
     *
     * @param fileName 文件名
     */
    public static void readFileByRandomAccess(String fileName) {
        RandomAccessFile randomFile = null;
        try {
            logger.info("随机读取一段文件内容：");
            randomFile = new RandomAccessFile(fileName, "r");
            long fileLength = randomFile.length();
            int beginIndex = (fileLength > 4) ? 4 : 0;
            randomFile.seek(beginIndex);
            byte[] bytes = new byte[10];
            int byteread = 0;
            while ((byteread = randomFile.read(bytes)) != -1) {
                System.out.write(bytes, 0, byteread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 显示输入流中还剩的字节数
     *
     * @param in
     */
    private static void showAvailableBytes(InputStream in) {
        try {
            logger.info(String.format("当前字节输入流中的字节数为:", in.available()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * RandomAccessFile追加文件
     *
     * @param fileName 文件名
     * @param content  追加的内容
     */
    public static void appendMethodByRandomAccessFile(String fileName,
                                                      String content) {
        System.out.println("RandomAccessFile追加文件");
        try {
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * FileWriter追加文件
     *
     * @param fileName
     * @param content
     */
    public static void appendMethodByFileWriter(String fileName, String content) {
        System.out.println("appendMethodByFileWriter");
        try {
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 转换BufferedImage 数据为byte数组
     *
     * @param bImage Image对象
     * @param format image格式字符串.如"gif","png"
     * @return byte数组
     */
    public static byte[] imageToBytes(BufferedImage bImage, String format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, format, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /**
     * 获取文件的前缀
     *
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
        if (StringUtils.isNotBlank(fileName)) {
            return fileName.split("\\.")[1];
        } else {
            return null;
        }
    }


    /**
     * 转化 TIFF 文件
     *
     * @param inputPath
     * @param outputPath
     * @throws IOException
     */
    public static void convertToTiff(String inputPath, String outputPath) throws IOException {
        File file = new File(inputPath);
        BufferedImage img = ImageIO.read(new FileInputStream(file));
        File myNewTIFF_File = new File(outputPath);
        ImageIO.write(img, "TIF", myNewTIFF_File);
    }

    /**
     * 获取文本内容
     *
     * @param fileName
     * @return
     */
    public static String getTxtFileContent(String fileName) {
        File file = new File(fileName);
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            do {
                tempString = reader.readLine();
                if (StringUtils.isNotEmpty(tempString)) {
                    sb.append(tempString);
                    sb.append("\n");
                }
            } while (tempString != null);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb.toString();
    }

    public static void writeToFile(String out, String[] fontFamily, int lineSize) {
        File file = new File(out);
        FileOutputStream fos;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int i = 0; i < lineSize; i++) {
                bw.write(fontFamily[i] + " 0 0 0 0 0\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 复制文件
     *
     * @param directory
     * @param prefix
     */
    public static String moveFile(String directory, String prefix) {
        File dir = new File(directory);
        if (dir.isDirectory()) {
            String grandParentDirectory = dir.getParent();
            File boxFile = new File(directory + File.separator + prefix + ".box");
            File trFile = new File(directory + File.separator + prefix + ".tr");

            File tempDir = new File(grandParentDirectory + File.separator + "boxtr");
            if (!tempDir.exists()) {
                tempDir.mkdir();
            }

            File tempBoxFile = new File(grandParentDirectory + File.separator + "boxtr" + File.separator + prefix + ".box");
            File tempTrFile = new File(grandParentDirectory + File.separator + "boxtr" + File.separator + prefix + ".tr");

            nioTransferCopy(boxFile, tempBoxFile);
            nioTransferCopy(trFile, tempTrFile);

            return grandParentDirectory + File.separator + "boxtr";
        }
        return null;

    }

    /**
     * 拷贝
     *
     * @param source
     * @param target
     */
    private static void nioTransferCopy(File source, File target) {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(target);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(inStream);
            close(in);
            close(outStream);
            close(out);
        }
    }

    /**
     * 获取父亲目录
     *
     * @param f
     * @return
     */
    public static String getParentDirectory(String f) {
        File file = new File(f);
        if (file.exists()) {
            if (file.isDirectory()) {
                return file.getParent();
            } else {
                return file.getParent();
            }
        }
        return null;
    }


    /**
     * 根据扩展名查询文件
     *
     * @param directory
     * @param suffix
     * @return
     */
    public static List<String> getFilesBySuffix(String directory, String suffix) {
        List<String> files = getFiles(directory);

        List<String> newFiles = new ArrayList<>();

        Iterator<String> it = files.iterator();
        while (it.hasNext()) {
            String x = it.next();
            if (x.endsWith(suffix)) {
                newFiles.add(x);
            }
        }
        return newFiles;
    }

    /**
     * 获取文件目录下的文件名
     *
     * @param path
     * @return
     */
    public static ArrayList<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i].toString());
            }
            if (tempList[i].isDirectory()) {
            }
        }
        return files;
    }

    /**
     * 读取文件返回 String
     *
     * @param fileName
     * @return
     */
    public static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当前目录下文件名
     *
     * @param realPath
     * @return
     */
    public static String getFileName(String realPath) {
        File file = new File(realPath);
        if (file.exists()) {
            return file.getName();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
//        String path = FileUtil.class.getResource("").getFile();
//        String fileName = path + "/temp.txt";
//        FileUtil.readFileByByte(fileName);
//        System.out.println("");
//        FileUtil.readFileByBytes(fileName);
//        System.out.println("");
//        FileUtil.readFileByChar(fileName);
//        System.out.println("");
//        FileUtil.readFileByChars(fileName);
//        System.out.println("");
//        FileUtil.readFileByLines(fileName);
//        System.out.println("");
//        FileUtil.readFileByRandomAccess(fileName);
//        System.out.println("");
//        String content = "\nnew append RandomAccessFile!";
//        FileUtil.appendMethodByRandomAccessFile(fileName, content);
//        FileUtil.appendMethodByRandomAccessFile(fileName,
//                "\nappend end RandomAccessFile");
//        FileUtil.readFileByLines(fileName);
//        System.out.println("");
//        FileUtil.appendMethodByFileWriter(fileName, content);
//        FileUtil.appendMethodByFileWriter(fileName,
//                "\nappend end appendMethodByFileWriter");
//        FileUtil.readFileByLines(fileName);
        /*String inputPath = "/Users/zacky/Desktop/tess/test.png";
        String ouputPath = "/Users/zacky/Desktop/tess/test.tif";
        FileUtil.convertToTiff(inputPath, ouputPath);*/
        //FileUtil.writeToFile("/Users/zacky/Desktop/tess/fontf", new String[]{"Sim", "Sim"}, 2);

        /*List<String> list = FileUtil.getFilesBySuffix("/Users/zacky/Desktop/work/upload/boxtr", "box");
        System.out.println(list.size());*/
//        System.out.println(getFileName("/Users/zacky/Desktop/PicHandle/original.tif"));
        //FileUtil.moveFile("/Users/zacky/Desktop/work/upload/1536736913851", "chiTra.SimSun.1536736913851");
    }


}
