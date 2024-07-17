package com.nx.nxbi.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 线程池配置类
 *
 * @author Ni Xiang
 */
@Configuration
public class ThreadPoolExecutorConfig {
    @Value("${thread-pool.core-pool-size}")
    private int corePoolSize;
    @Value("${thread-pool.maximum-pool-size}")
    private int maximumPoolSize;
    @Value("${thread-pool.keep-alive-time}")
    private long keepAliveTime;
    @Value("${thread-pool.work-queue-size}")
    private int workQueueSize;

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private int count = 1;

            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("线程" + count);
                count++;
                return thread;
            }
        };
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(workQueueSize);
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                workQueue,
                threadFactory,
                //拒绝策略
                new ThreadPoolExecutor.AbortPolicy());
    }
}
