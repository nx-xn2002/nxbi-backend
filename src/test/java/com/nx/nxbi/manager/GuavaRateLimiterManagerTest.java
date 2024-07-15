package com.nx.nxbi.manager;

import com.nx.nxbi.exception.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class GuavaRateLimiterManagerTest {
    @Resource
    private GuavaRateLimiterManager guavaRateLimiterManager;

    @Test
    public void test1() throws ExecutionException {
        guavaRateLimiterManager.doRateLimit("a");
        Assertions.assertThrows(BusinessException.class, () -> guavaRateLimiterManager.doRateLimit("a"));
    }
}