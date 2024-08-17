package com.lp.demo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageNoticeTypeEnum /*implements Strategy */{
    /**
     * 站内信
     */
    MAIL(1 << 0),
    /**
     * 邮件
     */
    EMAIL(1 << 1),
    /**
     * 短信
     */
    SMS(1 << 2),
    /**
     * 企业微信
     */
    WECOM(1 << 3),
    /**
     * 钉钉
     */
    DING_TALK(1 << 4),
    ;

    private final int code;

    public static MessageNoticeTypeEnum of(int code) {
        for (MessageNoticeTypeEnum alarmStatusEnum : MessageNoticeTypeEnum.values()) {
            if (alarmStatusEnum.getCode() == code) {
                return alarmStatusEnum;
            }
        }
        throw new UnsupportedOperationException("不支持的消息通知类型:" + code);
    }
}
