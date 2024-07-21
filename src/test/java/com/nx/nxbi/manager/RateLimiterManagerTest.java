package com.nx.nxbi.manager;

import com.nx.nxbi.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@Slf4j
class RateLimiterManagerTest {
    @Resource
    private RateLimiterManager rateLimiterManager;

    @Test
    public void test1() throws ExecutionException, InterruptedException {
        //第一次尝试请求，成功
        rateLimiterManager.doRateLimit("a");
        log.info("请求1");
        //0.5秒内尝试请求，抛出异常
        Assertions.assertThrows(BusinessException.class, () -> rateLimiterManager.doRateLimit("a"));
        log.info("请求2");
        Thread.sleep(500);
        //0.5秒后尝试请求，成功
        rateLimiterManager.doRateLimit("a");
        log.info("请求3");
    }
}