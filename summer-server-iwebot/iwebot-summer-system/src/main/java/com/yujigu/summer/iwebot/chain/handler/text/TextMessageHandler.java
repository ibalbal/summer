package com.yujigu.summer.iwebot.chain.handler.text;

import com.yujigu.summer.iwebot.wechat.body.WechatTextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 处理天气请求
 */
@Slf4j
@Component
public class TextMessageHandler extends TextMessageAbstract{
    @Override
    public void handleMessage(String receiver, WechatTextMessage wechatMessage) {
        log.info("TextMessageHandler 文本处理");
        if(china  != null){
            china.handleMessage(receiver, wechatMessage);
        }
    }
}
