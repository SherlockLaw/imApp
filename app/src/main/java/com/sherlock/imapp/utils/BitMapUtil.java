package com.sherlock.imapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sherlock.imapp.common.HttpProxy;
import com.sherlock.imapp.common.ServiceException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/2 0002.
 */

public class BitMapUtil {
    /**
     * Bitmap转换为文件
     *
     * @param toFile
     * @param quality
     * @param bitmap
     * @return
     */
    public static File bitmap2File(String toFile, int quality, Bitmap bitmap) {
        File captureFile = new File(toFile);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(captureFile);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return captureFile;
    }
    private static Map<String, WeakReference<Bitmap>> bitMapCache = new HashMap<>();
    /**
     * 根据url获取bitMap
     * @param url
     * @return
     */
    public static Bitmap getImageBitmap(String url){
        String fileName = url.replaceAll("\\/","-")+".jpg";
        String dirPath = FileUtil.picPath+generateRandomDir(fileName);
        String filePath = dirPath + fileName;
        File file = new File(filePath);
        Bitmap bitmap;
        // 内存缓存
        WeakReference<Bitmap> reference = bitMapCache.get(filePath);
        if (reference!=null) {
            bitmap = reference.get();
            if (bitmap!=null) {
                return bitmap;
            }
        }
        //本地缓存不存在
        if (!file.exists()) {
            bitmap = HttpProxy.getImageBitmap(url);
            if (bitmap!=null) {
                FileUtil.createFileIfNotExist(dirPath,fileName);
                File newFile = bitmap2File(filePath,100,bitmap);
            }
        } else {
            bitmap = decodeByFile(filePath);
        }
        //图片加入内存缓存
        bitMapCache.put(filePath,new WeakReference<Bitmap>(bitmap));
        return bitmap;
    }

    /**
     * 获得hashcode 生成二级目录
     * @param fileName
     * @return
     */
    public static String generateRandomDir(String fileName){
        int hashCode = fileName.hashCode();//得到它的hashcode编码
        //一级目录
        int d1 = hashCode & 0xf;
        //二级目录
        int d2 = (hashCode >> 4) & 0xf;
        return "/"+d1+"/"+d2+"/";
    }

    public static Bitmap decodeByFile(String path){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 1;
        Bitmap bitmap = BitmapFactory.decodeFile(path, opt);
        return bitmap;
    }

    public static String getShowPicUrl(String srcImgUrl,int srcWidth,int srcHeight,int maxWidth){
        int dotIdx = srcImgUrl.lastIndexOf('.');
        if (dotIdx==-1) {
            throw new ServiceException("图片路径有误");
        }
        int width = srcWidth>maxWidth ? maxWidth : srcWidth;
//        int height = width*srcHeight/srcWidth;
        StringBuilder sb = new StringBuilder();
        sb.append(srcImgUrl.substring(0, dotIdx))
                .append("_").append(width).append("x").append(srcHeight)
                .append(srcImgUrl.substring(dotIdx, srcImgUrl.length()));
        String result = sb.toString();
        return result;
    }
}
