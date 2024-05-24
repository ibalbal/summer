package com.yujigu.summer.iwebot.chain.handler.system;

import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.wechat.body.WechatSystemMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SystemMessageHandler extends SystemMessageAbstract{
    @Override
    public void execute(String receiver, WechatSystemMessage wechatSystemMessage) {
        log.info("系统处理");
        if (china != null){
            china.handleMessage(receiver, wechatSystemMessage);
        }
    }

    @Override
    public boolean isChain(WechatMessage wechatMessage) {
        return true;
    }
}
