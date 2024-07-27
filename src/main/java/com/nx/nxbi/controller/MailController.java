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
    @Value("${email.sender-address}")
    private String senderAddress;
    @Value("${email.auth-code}")
    private String authCode;

    @PostMapping("/getMailCode")
    public BaseResponse<Boolean> getMailCode(String targetEmail) {
        // 随机生成六位数验证码
        String code = String.valueOf(new Random().nextInt(899999) + 100000);
        try {
            SendMailUtil.sendEmail(senderAddress, authCode, targetEmail, "NX智能BI邮箱验证", "您好,您的验证码为" + code + " , " +
                    "5分钟内有效");
        } catch (Exception e) {
            log.error("发送邮件验证码失败, targetEmail -> {}", targetEmail, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        log.info("发送成功, 收件人:[{}], 验证码:[{}]", targetEmail, code);
        return ResultUtils.success(true);
    }
}
