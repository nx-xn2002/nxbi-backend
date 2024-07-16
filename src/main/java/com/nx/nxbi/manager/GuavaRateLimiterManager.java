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

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * 提供基于 guava 的限流服务
 *
 * @author Ni Xiang
 */
@Service
@Slf4j
public class GuavaRateLimiterManager {
    @Resource
    private LoadingCache<String, RateLimiter> rateLimiterCache;

    /**
     * 限流操作
     *
     * @param key 区分不同限流器
     * @author Ni Xiang
     */
    public void doRateLimit(String key) throws ExecutionException {
        RateLimiter limiter = rateLimiterCache.get(key);
        boolean canDo = limiter.tryAcquire();
        if (!canDo) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }
    }
}
