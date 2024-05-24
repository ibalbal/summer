package com.yujigu.summer.iwebot.chain.message;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import com.yujigu.summer.iwebot.chain.TextMessageChainHandler;
import com.yujigu.summer.iwebot.wechat.EnumsMsgType;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.chain.factory.Message;
import com.yujigu.summer.iwebot.wechat.body.WechatTextMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class _1_Message implements Message {
    @Override
    public EnumsMsgType execute(String sender, String message) {
        log.info("执行1_Message");
        TextMessageChainHandler chainHandler = SpringUtil.getBean(TextMessageChainHandler.class);
        WechatMessage wechatMessage = JSONObject.parseObject(message, WechatTextMessage.class);
        chainHandler.handleMessage(sender, wechatMessage);
        return EnumsMsgType._1;
    }
}
