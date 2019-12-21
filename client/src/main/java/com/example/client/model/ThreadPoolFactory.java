package com.example.client.model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2016-2020
 * FileName: ThreadPoolFactory
 * Author: wei.zheng
 * Date: 2019/12/21 14:21
 * Description: 线程池工具类
 */
public class ThreadPoolFactory {
    public static ExecutorService getExecutorService() {
        int poolSize = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 0;
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(512);
        RejectedExecutionHandler policy = new ThreadPoolExecutor.DiscardPolicy();

        return new ThreadPoolExecutor(poolSize, poolSize, keepAliveTime,
                TimeUnit.SECONDS, queue, policy);
    }
}
