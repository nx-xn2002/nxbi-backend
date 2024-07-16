package com.nx.nxbi.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 限流器缓存配置类
 *
 * @author Ni Xiang
 */
@Configuration
public class RateLimiterCacheConfig {
    @Value("${rate-limiter.initial-capacity}")
    private int initialCapacity;
    @Value("${rate-limiter.maximum-size}")
    private int maximumSize;
    @Value("${rate-limiter.permits-per-second}")
    private double permitsPerSecond;

    @Bean
    public LoadingCache<String, RateLimiter> rateLimiterCache() {
        return CacheBuilder.newBuilder()
                //初始容量
                .initialCapacity(initialCapacity)
                //最大容量
                .maximumSize(maximumSize)
                .build(new CacheLoader<String, RateLimiter>() {
                    //加载方法，当 get 无法获取到指定的 RateLimiter 时，将其加载到缓存里
                    @Override
                    public RateLimiter load(String key) throws Exception {
                        return RateLimiter.create(permitsPerSecond);
                    }
                });
    }
}
