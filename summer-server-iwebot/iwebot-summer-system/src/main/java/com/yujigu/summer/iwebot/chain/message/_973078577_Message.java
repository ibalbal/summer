package com.yujigu.summer.iwebot.chain.message;

import com.yujigu.summer.iwebot.wechat.EnumsMsgType;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.chain.factory.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class _973078577_Message implements Message {
    @Override
    public EnumsMsgType execute(String receiver, String message) {
        log.info("执行973078577_Message");
        return EnumsMsgType._973078577;
    }
}
