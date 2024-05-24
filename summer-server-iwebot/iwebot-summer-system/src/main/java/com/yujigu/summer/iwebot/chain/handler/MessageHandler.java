package com.yujigu.summer.iwebot.chain.handler;

import com.yujigu.summer.iwebot.wechat.body.WechatMessage;

public abstract class MessageHandler {
    protected MessageHandler china;

    protected abstract boolean isChain(WechatMessage wechatMessage);

    public void handleMessage(String receiver, WechatMessage wechatMessage){
        if (this.isChain(wechatMessage)){
            this.executeMessage(receiver, wechatMessage);
        }else if (china != null){
            china.handleMessage(receiver, wechatMessage);
        }
    }
    protected abstract void executeMessage(String receiver, WechatMessage wechatMessage);
}
