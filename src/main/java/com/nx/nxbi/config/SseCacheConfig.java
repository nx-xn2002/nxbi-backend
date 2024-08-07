package com.nx.nxbi.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
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
        return Caffeine.newBuilder()
                //初始容量
                .initialCapacity(initialCapacity)
                //最大容量
                .maximumSize(maximumSize)
                .build(
                        key -> {
                            return new SseEmitter(timeOut);
                        }
                );
    }
}
