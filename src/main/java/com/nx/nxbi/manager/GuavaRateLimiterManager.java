package com.nx.nxbi.manager;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.nx.nxbi.common.ErrorCode;
import com.nx.nxbi.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentMap;

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
    private final ConcurrentMap<String, RateLimiter> map = Maps.newConcurrentMap();

    /**
     * 限流操作
     *
     * @param key 区分不同限流器
     * @author Ni Xiang
     */
    public void doRateLimit(String key) {
        //每秒2次请求
        map.putIfAbsent(key, RateLimiter.create(2.0));
        RateLimiter limiter = map.get(key);
        boolean canDo = limiter.tryAcquire();
        if (!canDo) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }
    }
}
