package com.guoye.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class FileUtils {
    private static String img_url;
    //绝对路径修改 start
    public static String IMG_PATH = "/usr/local/static/imgs/";
    public static String FILE_PATH = "/usr/local/static/file/";

    public static String getImg_url() {
        return img_url;
    }

    @Value("${img_url}")
    public void setImg_url(String img_url) {
        FileUtils.img_url = img_url;
    }

    /**
     * @param input
     * @author
     */
    public static String storeImg(InputStream input, String fileName, String filePath) {
        String wholeFilePath = "";
        try {
            String newPath = IMG_PATH + "/" + filePath;
            createFolder(newPath);
            wholeFilePath = newPath + "/" + fileName;
            File file = new File(wholeFilePath);
            if (file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(wholeFilePath);
            byte[] readByte = new byte[1024];
            while ((input.read(readByte)) != -1) {
                fos.write(readByte);
            }
            input.close();
            fos.close();
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("chmod -R 777 " + newPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img_url + filePath + "/" + fileName;
    }


    private static BufferedWriter output = null;
    private static BufferedReader input = null;

    /**
     * 复制整个文件夹内容    复制查找到的文件夹
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath) {
        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }
    }

    /**
     * 删除文件夹里面的所有文件
     *
     * @param path String 文件夹路径 如 c:/fqf
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
            }
        }
    }

    /**
     * 删除文件夹
     *
     * @param folderPath String
     * @return boolean
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            System.out.println("删除文件夹操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param filePathAndName String
     * @return boolean
     */
    public static void delFile(String filePathAndName) {
        try {
            if (filePathAndName == null || "".equals(filePathAndName)) {
                return;
            }
            File myDelFile = new File(filePathAndName);
            if (!myDelFile.exists()) {

            } else {
                myDelFile.delete();
            }
        } catch (Exception e) {
            System.out.println("删除文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 删除rar文件
     *
     * @param filePath String 文件路径及名称 如c:/fqf.txt
     * @return boolean
     */
    public static void delRarFile(String filePath) {
        try {
            File dir = new File(filePath);//文件目录
            File[] files = dir.listFiles();
            String fileName = "";
            for (File f : files) {
                fileName = f.getName();
                if (fileName.endsWith(".rar")) {
                    if (f.delete()) {
                        System.out.println("已删除文件：" + fileName);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("删除rar文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * @param srcFile
     * @param serverPath
     * @throws Exception
     * @Description: 电子签章图片上传服务器<br/>
     * @author gaojunlong
     * @serial 2013-10-10 下午4:13:44
     * @since JDK 1.6
     */
    public static void uploadImgToServer(File srcFile, String serverPath)
            throws Exception {
        // 开始上传
        InputStream is = null;
        OutputStream os = null;
        is = new FileInputStream(srcFile);
        os = new FileOutputStream(serverPath);
        byte[] buffer = new byte[1024];
        int length = 0;
        while (-1 != (length = (is.read(buffer)))) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.close();
    }

    public static void createFolder(String folderPath) {
        File file = new File(folderPath);
        //如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            System.out.println("---------create folder-------------" + folderPath);
            file.mkdir();
        } else {
            System.out.println(folderPath + "//目录存在");
        }
    }

    /**
     * @param strUrl
     * @return
     * @Description: 通过url抓取页面流
     * @author:yangshi2@creditase.cn 下午2:19:31
     */
    public static InputStream getInputStreamByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            return inStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写入数据到txt文件中
     *
     * @param filePath txt文件绝对路径
     * @param content  待写入内容
     * @author chencl
     * @date 2016年12月7日 下午3:07:12
     */
    public static void contentToTxt(String filePath, String content) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                f.createNewFile(); // 不存在则创建
            }
            input = new BufferedReader(new FileReader(f));
            String str = new String();// 原有txt内容
            String s1 = new String(); // 内容更新
            while ((str = input.readLine()) != null) {
                s1 += str + "\r\n";
            }
            s1 += content;
            output = new BufferedWriter(new FileWriter(f));
            output.write(s1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 去掉字符串右边的空格
     *
     * @param str 要处理的字符串
     * @return 处理后的字符串
     */
    public static String rightTrim(String str) {
        if (str == null) {
            return "";
        }
        int length = str.length();
        for (int i = length - 1; i >= 0; i--) {
            if (str.charAt(i) != 0x20) {
                break;
            }
            length--;
        }
        return str.substring(0, length);
    }
}
