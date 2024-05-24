package com.yujigu.summer.iwebot.chain.handler.system;

import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.wechat.body.WechatSystemMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SystemPatMessageHandler extends SystemMessageAbstract{
    @Override
    public void execute(String receiver, WechatSystemMessage wechatSystemMessage) {
        log.info("拍了拍");
    }

    @Override
    public boolean isChain(WechatMessage wechatMessage) {
        WechatSystemMessage wechatSystemMessage = (WechatSystemMessage)wechatMessage;
        return wechatSystemMessage.content.startsWith("拍了拍我");
    }
}
