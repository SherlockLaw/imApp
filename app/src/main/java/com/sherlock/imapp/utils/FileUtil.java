package com.sherlock.imapp.utils;

import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSONReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * Created by Administrator on 2018/6/2 0002.
 */

public class FileUtil {
    /**
     * 所有文件的基础路径
     */
    public static final String basePath = Environment.getExternalStorageDirectory() + "/imapp/";
    /**
     * 图片基础路径
     */
    public static final String picPath = basePath+"/pic/";
    public static final String picUserParentPath = picPath+"/user/";
    public static final String picImParentPath = picPath+"/im/";

    public static final String configureFileName = "config.json";
    /**
     * 注册页面的头像
     */
    public static final String regisgerHeadPicName = "register_headpic.jpg";
    public static final String regisgerHeadPicNamePath = picPath+regisgerHeadPicName;
    public static final String regisgerGroupHeadPicName = "register_group_headpic.jpg";
    public static final String regisgerGroupHeadPicNamePath = picPath+regisgerGroupHeadPicName;

    private static final String charsetName = "UTF-8";

    /**
     * 从文件中读取json字符串
     * @return
     */
    public static String readConfig(){
        createFileIfNotExist(basePath, configureFileName);
        //读取文件(字节流)
        StringBuilder sb = new StringBuilder(200);
        Reader in = null;
        try {
            in = new InputStreamReader(new FileInputStream(basePath + configureFileName),charsetName);
            //循环取出数据
            char[] buf = new char[1024];
            while (in.read(buf) != -1) {
                sb.append(buf);
            }
            //关闭流
        } catch (IOException  e) {
            e.printStackTrace();
        } finally {
            try{
                if (in!=null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 写入配置文件
     * @param config 配置的json字符串
     */
    public static void writeConfig(String config){
        createFileIfNotExist(basePath, configureFileName);
        Writer out = null;
        try{
            out = new OutputStreamWriter(new FileOutputStream(basePath + configureFileName),charsetName);
            out.write(config);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if (out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     *  创建路径
     * @param dirPath
     */
    public static void createDir(String dirPath){
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            boolean created = dirFile.mkdirs();
            Log.d("result", "create result:" + created);
        }
    }

    /**
     * 如果文件不存在，创建文件，并且自动创建路径
     * @param dirPath
     * @param fileName
     */
    public static void createFileIfNotExist(String dirPath,String fileName){
        createDir(dirPath);
        File image = new File(dirPath+fileName);
        if (!image.exists()) {
            try {
                image.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("create file error",e.getMessage());
            }
        }
    }
}
