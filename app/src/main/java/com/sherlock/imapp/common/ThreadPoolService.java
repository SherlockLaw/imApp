package com.sherlock.imapp.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class ThreadPoolService {
    private static ThreadPoolService instance = new ThreadPoolService();

    private ExecutorService executorService ;
    {
        executorService= Executors.newCachedThreadPool(new MyThreadFactory());
    }

    public static void execute(Runnable task){
        instance.executorService.execute(task);
    }
    public static Future<?> submit(){
        //使用submit的时候，必须等待调用Future.get()的时候才能抛出异常（afterExecute）
        return null;
    }
    private static class MyThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setUncaughtExceptionHandler(ExceptionHandler.getInstance());
            return t;
        }
    }
}

