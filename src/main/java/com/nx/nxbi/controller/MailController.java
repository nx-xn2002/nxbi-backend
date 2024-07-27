package com.nx.nxbi.controller;

import com.nx.nxbi.common.BaseResponse;
import com.nx.nxbi.common.ErrorCode;
import com.nx.nxbi.common.ResultUtils;
import com.nx.nxbi.common.SendMailUtil;
import com.nx.nxbi.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import com.github.benmanes.caffeine.cache.Cache;

import java.util.Random;

/**
 * mail controller
 *
 * @author Ni Xiang
 */
@RestController
@RequestMapping("/mail")
@Slf4j
public class MailController {
    @Value("${email-code.sender-address}")
    private String senderAddress;
    @Value("${email-code.auth-code}")
    private String authCode;
    @Resource
    private Cache<String, Integer> mailCodeCache;

    @PostMapping("/getMailCode")
    public BaseResponse<Boolean> getMailCode(String targetEmail) {
        if (!isValidEmail(targetEmail)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
        }
        Integer presentCode = mailCodeCache.getIfPresent(targetEmail);
        if (presentCode == null) {
            // 随机生成六位数验证码
            int code = new Random().nextInt(899999) + 100000;
            try {
                SendMailUtil.sendEmail(senderAddress, authCode, targetEmail, "NX智能BI邮箱验证", "您好,您的验证码为" + code + " , " +
                        "5分钟内有效");
            } catch (Exception e) {
                log.error("发送邮件验证码失败, targetEmail -> {}", targetEmail, e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
            log.info("发送成功, 收件人:[{}], 验证码:[{}]", targetEmail, code);
            return ResultUtils.success(true);
        } else {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }
    }

    /**
     * 判断邮箱格式
     *
     * @param email email
     * @return boolean
     * @author Ni Xiang
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (email == null) {
            return false;
        }
        return email.matches(emailRegex);
    }
}
