package com.nx.nxbi.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import com.nx.nxbi.common.ErrorCode;
import com.nx.nxbi.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/**
 * 提供基于 guava 的限流服务
 *
 * @author Ni Xiang
 */
@Service
@Slf4j
public class GuavaRateLimiterManager {
    /**
     * 限流器与 key 的映射关系
     */
    private final LoadingCache<String, RateLimiter> cache = CacheBuilder.newBuilder()
            //初始容量
            .initialCapacity(5)
            //最大容量
            .maximumSize(20)
            .build(new CacheLoader<String, RateLimiter>() {
                //加载方法，当 get 无法获取到指定的 RateLimiter 时，将其加载到缓存里
                @Override
                public RateLimiter load(String key) throws Exception {
                    return RateLimiter.create(2.0);
                }
            });

    /**
     * 限流操作
     *
     * @param key 区分不同限流器
     * @author Ni Xiang
     */
    public void doRateLimit(String key) throws ExecutionException {
        RateLimiter limiter = cache.get(key);
        boolean canDo = limiter.tryAcquire();
        if (!canDo) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }
    }
}
