package com.sherlock.imapp.presenter;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.common.ThreadPoolService;
import com.sherlock.imapp.utils.BitMapUtil;
import com.sherlock.imapp.utils.FileUtil;

/**
 * Created by Administrator on 2018/6/2 0002.
 */

public class BitMapPresenter {

    /**
     * 显示头像
     * @param fileName
     * @param url
     * @param imageView
     */
    public static void showUserHeadPic(final String fileName, final String url, ImageView imageView){
        showImageBitmap(FileUtil.picUserParentPath,fileName,url,imageView);
    }

    /**
     * 显示Im图片
     * @param fileName
     * @param url
     * @param imageView
     */
    public static void showImPic(final String fileName, final String url, ImageView imageView){
        showImageBitmap(FileUtil.picImParentPath,fileName,url,imageView);
    }
    private static void showImageBitmap(final String parentPath, final String fileName, final String url, ImageView imageView) {
        Runnable runnable = new Runnable() {
            private String parentPath;
            private String fileName;
            private String url;
            private ImageView imageView;
            public Runnable init(final String parentPath, final String fileName, final String url,ImageView imageView){
                this.parentPath = parentPath;
                this.fileName = fileName;
                this.url = url;
                this.imageView = imageView;
                return this;
            }
            @Override
            public void run() {
                Bitmap bitmap = BitMapUtil.getImageBitmap(parentPath,fileName, url);
//                imageView.setImageBitmap(bitmap);
                MyApplication.getInstance().runOnUiThread(new Runnable() {
                    private Bitmap bitmap;
                    public Runnable init(Bitmap bitmap){
                        this.bitmap = bitmap;
                        return this;
                    }
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                }.init(bitmap));
            }
        }.init(parentPath,fileName,url,imageView);
        ThreadPoolService.execute(runnable);
    }

}
