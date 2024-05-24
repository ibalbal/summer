package com.yujigu.summer.iwebot.chain.handler.system;

import com.yujigu.summer.iwebot.chain.handler.MessageHandler;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.wechat.body.WechatSystemMessage;

/**
 * 文本消息抽象请求类
 */
public abstract class SystemMessageAbstract extends MessageHandler {
    public void setMessage(SystemMessageAbstract china) {
        this.china = china;
    }

    protected abstract void execute(String receiver, WechatSystemMessage wechatMessage);

    @Override
    protected void executeMessage(String receiver, WechatMessage wechatMessage) {
        execute(receiver, (WechatSystemMessage) wechatMessage);
    }
}
