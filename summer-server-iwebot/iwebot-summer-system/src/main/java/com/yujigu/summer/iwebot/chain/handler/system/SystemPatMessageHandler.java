package com.yujigu.summer.iwebot.chain.handler.system;

import com.yujigu.summer.iwebot.wechat.body.WechatSystemMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SystemPatMessageHandler extends SystemMessageAbstract{
    @Override
    public void handleMessage(String receiver, WechatSystemMessage wechatSystemMessage) {
        if (wechatSystemMessage.content.endsWith("拍了拍我")){
            log.info("拍了拍");
        }else if (china  != null){
            china.handleMessage(receiver, wechatSystemMessage);
        }
    }
}
