package com.nx.nxbi.manager;


import com.github.benmanes.caffeine.cache.LoadingCache;
import com.nx.nxbi.common.ErrorCode;
import com.nx.nxbi.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * sse服务
 *
 * @author Ni Xiang
 */
@Service
@Slf4j
public class SseManager {
    @Resource
    private LoadingCache<Long, SseEmitter> sseCache;

    /**
     * 获取连接
     *
     * @param userId user id
     * @return {@link SseEmitter }
     * @author Ni Xiang
     */
    public SseEmitter getConn(Long userId) throws ExecutionException {
        SseEmitter emitter = sseCache.get(userId);
        // 注册超时回调，超时后触发
        emitter.onTimeout(() -> {
            log.info("连接已超时，正准备关闭，userId = {}", userId);
            sseCache.invalidate(userId);
        });
        // 注册完成回调，调用 emitter.complete() 触发
        emitter.onCompletion(() -> {
            log.info("连接已关闭，正准备释放，userId = {}", userId);
            sseCache.invalidate(userId);
            log.info("连接已释放，userId = {}", userId);
        });
        // 注册异常回调，调用 emitter.completeWithError() 触发
        emitter.onError(throwable -> {
            log.error("连接已异常，正准备关闭，userId = {}", userId, throwable);
            sseCache.invalidate(userId);
        });
        sseCache.put(userId, emitter);
        return emitter;
    }

    /**
     * 向指定用户发送消息
     *
     * @param userId  user id
     * @param message message
     * @author Ni Xiang
     */
    public void doChat(Long userId, String message) {
        try {
            SseEmitter emitter = getConn(userId);
            emitter.send(message);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息发送失败");
        }
    }
}
