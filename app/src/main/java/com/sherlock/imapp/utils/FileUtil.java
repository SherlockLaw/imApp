package com.sherlock.imapp.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

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
    /**
     * 注册页面的头像
     */
    public static final String regisgerHeadPicName = "register_headpic.jpg";
    public static final String regisgerHeadPicNamePath = picPath+regisgerHeadPicName;
    public static final String regisgerGroupHeadPicName = "register_group_headpic.jpg";
    public static final String regisgerGroupHeadPicNamePath = picPath+regisgerGroupHeadPicName;
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
