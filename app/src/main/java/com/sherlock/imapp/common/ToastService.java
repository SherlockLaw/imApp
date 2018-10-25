package com.sherlock.imapp.common;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/5/7 0007.
 */

public class ToastService extends Service{
    private Handler handler = new Handler(Looper.getMainLooper());

    private static ToastService toastService;
    private Context ctx;
    private ToastService(Context ctx) {
        this.ctx = ctx;
    }
    public static void init(Context ctx) {
        if (toastService == null) {
            synchronized (ToastService.class) {
                if (toastService == null) {
                    toastService = new ToastService(ctx);
                }
            }
        }
    }

    public static void toastMsg(final String msg) {
        toastService.toastMsg0(msg);
    }

    private void toastMsg0(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
