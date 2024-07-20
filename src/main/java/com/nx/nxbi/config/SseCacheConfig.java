package com.nx.nxbi.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 服务器推送事件缓存配置类
 *
 * @author Ni Xiang
 */
@Configuration
public class SseCacheConfig {
    @Value("${sse.initial-capacity}")
    private int initialCapacity;
    @Value("${sse.maximum-size}")
    private int maximumSize;
    @Value("${sse.time-out}")
    private long timeOut;

    @Bean
    public LoadingCache<Long, SseEmitter> sseCache() {
        return CacheBuilder.newBuilder()
                //初始容量
                .initialCapacity(initialCapacity)
                //最大容量
                .maximumSize(maximumSize)
                .build(new CacheLoader<Long, SseEmitter>() {
                    //加载方法，当 get 无法获取到指定的 SseEmitter 时，将其加载到缓存里
                    @NotNull
                    @Override
                    public SseEmitter load(@NotNull Long key) throws Exception {
                        return new SseEmitter(timeOut);
                    }
                });
    }
}
