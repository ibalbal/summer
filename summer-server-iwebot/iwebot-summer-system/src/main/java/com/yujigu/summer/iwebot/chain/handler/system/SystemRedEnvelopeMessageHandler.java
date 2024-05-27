package com.yujigu.summer.iwebot.chain.handler.system;

import com.yujigu.summer.iwebot.entity.ResultMessage;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
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
    public ResultMessage execute(String receiver, WechatSystemMessage wechatSystemMessage) {
        log.info("红包处理");
        return null;
    }

    @Override
    public boolean isChain(WechatMessage wechatMessage) {
        WechatSystemMessage wechatSystemMessage = (WechatSystemMessage)wechatMessage;
        return wechatSystemMessage.content.startsWith("收到红包");
    }
}
