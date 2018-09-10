package com.chineseall.util;


import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 17:35.
 */
public class FileUtil {

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
                } catch (IOException e1) {
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
            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
            // 一次读多个字节
            byte[] tempbytes = new byte[100];
            int byteread = 0;
            in = new FileInputStream(file);
            showAvailableBytes(in);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
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
    public static void readFileByChar(String fileName) {
        File file = new File(fileName);
        Reader reader = null;

        try {
            System.out.println("以字符为单位读取文件内容，一次读多个字节：");
            // 一次读多个字符
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(file));
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while ((charread = reader.read(tempchars)) != -1) {
                // 同样屏蔽掉r不显示
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
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     *
     * @param fileName 文件名
     */
    public static void readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        try {
            System.out.println("以字符为单位读取文件内容，一次读多个字节：");
            // 一次读多个字符
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(file));
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while ((charread = reader.read(tempchars)) != -1) {
                // 同样屏蔽掉r不显示
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
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
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
            System.out.println("随机读取一段文件内容：");
            // 打开一个随机访问文件流，按只读方式
            randomFile = new RandomAccessFile(fileName, "r");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 读文件的起始位置
            int beginIndex = (fileLength > 4) ? 4 : 0;
            // 将读文件的开始位置移到beginIndex位置。
            randomFile.seek(beginIndex);
            byte[] bytes = new byte[10];
            int byteread = 0;
            // 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
            // 将一次读取的字节数赋给byteread
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
            System.out.println("当前字节输入流中的字节数为:" + in.available());
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
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
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
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
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
            String tempString = null;
            do {
                tempString = reader.readLine();
                sb.append(tempString);
                sb.append("\n");
                if (tempString != null && tempString.length() == 0) {
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

    public static String test(String fileName) throws ParserConfigurationException, IOException, SAXException {
        byte[] bytes = readFileByBytes(fileName);
        return new String(bytes);
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
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
        String inputPath = "/Users/zacky/Desktop/tess/test.png";
        String ouputPath = "/Users/zacky/Desktop/tess/test.tif";
        FileUtil.convertToTiff(inputPath, ouputPath);
    }

}
