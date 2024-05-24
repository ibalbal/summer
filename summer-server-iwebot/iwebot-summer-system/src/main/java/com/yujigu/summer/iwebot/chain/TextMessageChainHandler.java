package com.yujigu.summer.iwebot.chain;

import com.yujigu.summer.iwebot.chain.handler.text.*;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.wechat.body.WechatTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 文本责任链模式 - 处理
 */
@Component
@Configuration
public class TextMessageChainHandler implements MessageChainHandler {

    @Autowired
    TextMessageHandler textMessageHandler;

    @Autowired
    TextUpdateSystemHandler textUpdateSystemHandler;

    @Autowired
    TextWeatherHandler textWeatherHandler;

    @Autowired
    TextSongHandler textSongHandler;

    @Autowired
    TextNovelHandler textNovelHandler;

    @Autowired
    TextShortVideoHandler textShortVideoHandler;

    @Override
    public void handleMessage(String sender, WechatMessage wechatMessage) {
        textMessageHandler.setMessage(textUpdateSystemHandler);
        textUpdateSystemHandler.setMessage(textWeatherHandler);
        textWeatherHandler.setMessage(textSongHandler);
        textSongHandler.setMessage(textNovelHandler);
        textNovelHandler.setMessage(textShortVideoHandler);

        textMessageHandler.handleMessage(sender, (WechatTextMessage) wechatMessage);
    }
}
