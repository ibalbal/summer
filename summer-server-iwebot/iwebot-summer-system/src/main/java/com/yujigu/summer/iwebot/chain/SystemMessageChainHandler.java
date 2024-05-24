package com.yujigu.summer.iwebot.chain;

import com.yujigu.summer.iwebot.chain.handler.system.*;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.wechat.body.WechatSystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 系统消息处理
 */
@Component
@Configuration
public class SystemMessageChainHandler implements MessageChainHandler {

    @Autowired
    private SystemMessageHandler systemMessageHandler;

    @Autowired
    private SystemGroupMessageHandler systemGroupMessageHandler;

    @Autowired
    private SystemPatMessageHandler systemPatMessageHandler;

    @Autowired
    private SystemRedEnvelopeMessageHandler systemRedEnvelopeMessageHandler;

    @Override
    public void handleMessage(String sender, WechatMessage wechatMessage) {
        systemMessageHandler.setMessage(systemPatMessageHandler);
        systemPatMessageHandler.setMessage(systemGroupMessageHandler);
        systemGroupMessageHandler.setMessage(systemRedEnvelopeMessageHandler);
        systemMessageHandler.handleMessage(sender, (WechatSystemMessage) wechatMessage);
    }
}
