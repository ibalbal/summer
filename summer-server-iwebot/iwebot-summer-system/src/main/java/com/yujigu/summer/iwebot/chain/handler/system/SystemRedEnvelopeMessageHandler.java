package com.yujigu.summer.iwebot.chain.handler.system;

import com.yujigu.summer.iwebot.wechat.body.WechatSystemMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 红包处理
 */
@Slf4j
@Component
public class SystemRedEnvelopeMessageHandler extends SystemMessageAbstract{
    @Override
    public void handleMessage(String receiver, WechatSystemMessage wechatSystemMessage) {
        if (wechatSystemMessage.content.startsWith("收到红包")){
            log.info("红包处理");
        }else if (china  != null){
            china.handleMessage(receiver, wechatSystemMessage);
        }
    }
}
