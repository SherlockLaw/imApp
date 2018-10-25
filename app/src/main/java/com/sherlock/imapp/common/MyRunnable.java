package com.sherlock.imapp.common;

/**
 * Created by Administrator on 2018/5/5 0005.
 */

public abstract class MyRunnable implements Runnable {
    protected Object callback;
    public MyRunnable(Object callback){
        this.callback = callback;
    }
}
