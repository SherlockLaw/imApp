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
     * @param url
     * @param imageView
     */
    public static void showUserHeadPic(final String url, ImageView imageView){
        showImageBitmap(url,imageView);
    }

    /**
     * 显示Im图片
     * @param url
     * @param imageView
     */
    public static void showImPic(final String url, ImageView imageView){
        showImageBitmap(url,imageView);
    }
    private static void showImageBitmap(final String url, final ImageView imageView) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitMapUtil.getImageBitmap(url);
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
        };
        ThreadPoolService.execute(runnable);
    }

}
