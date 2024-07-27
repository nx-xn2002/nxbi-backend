package com.nx.nxbi.common;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;

/**
 * send mail util
 *
 * @author Ni Xiang
 */
public class SendMailUtil {
    /**
     * send email
     *
     * @param senderAddress 发送地址
     * @param authCode      鉴权码
     * @param targetAddress 目标地址
     * @param subject       邮件标题
     * @param content       邮件正文
     * @author Ni Xiang
     */
    public static void sendEmail(String senderAddress, String authCode, String targetAddress, String subject,
                                 String content) {
        MailAccount account = new MailAccount();
        account.setHost("smtp.163.com");
        account.setPort(465);
        account.setStarttlsEnable(true);
        account.setAuth(true);
        account.setFrom("NX智能BI<" + senderAddress + ">");
        account.setPass(authCode);
        MailUtil.send(account, targetAddress, subject, content, false);
    }
}
