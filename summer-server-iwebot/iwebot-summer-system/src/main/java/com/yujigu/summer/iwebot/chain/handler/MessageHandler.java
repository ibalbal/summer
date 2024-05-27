package com.yujigu.summer.iwebot.chain.handler;

import com.yujigu.summer.iwebot.config.MessageConfig;
import com.yujigu.summer.iwebot.entity.ResultMessage;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public abstract class MessageHandler {

    @Autowired
    private MessageConfig messageConfig;

    protected MessageHandler china;

    protected abstract boolean isChain(WechatMessage wechatMessage);

    protected abstract ResultMessage executeMessage(String receiver, WechatMessage wechatMessage);

    public void handleMessage(String receiver, WechatMessage wechatMessage){
        if (this.isChain(wechatMessage)){
            ResultMessage resultMessage = this.executeMessage(receiver, wechatMessage);
            //发送请求
            if (resultMessage != null){
                resultMessage.execute(messageConfig.getUrl(), messageConfig.getToken());
            }
        }else if (china != null){
            china.handleMessage(receiver, wechatMessage);
        }
    }

    public String messageRequestToken(){
        return null;
    }

    public String  messageRequestUrl(){
        return null;
    }
}
