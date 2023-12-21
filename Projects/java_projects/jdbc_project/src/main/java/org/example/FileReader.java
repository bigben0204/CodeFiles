package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReader {
    public static String readContent(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("file not exist. file path: " + filePath);
        }
        Long fileLength = file.length();     // 获取文件长度
        byte[] filecontent = new byte[fileLength.intValue()];
        try {
            FileInputStream in = new FileInputStream(filePath);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(filecontent);// 返回文件内容,默认编码
    }
}
