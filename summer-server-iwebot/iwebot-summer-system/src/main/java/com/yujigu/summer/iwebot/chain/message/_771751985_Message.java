package com.yujigu.summer.iwebot.chain.message;

import com.yujigu.summer.iwebot.wechat.EnumsMsgType;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.chain.factory.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class _771751985_Message implements Message {
    @Override
    public EnumsMsgType execute(String receiver, String message) {
        log.info("执行771751985_Message");
        return EnumsMsgType._771751985;
    }
}
