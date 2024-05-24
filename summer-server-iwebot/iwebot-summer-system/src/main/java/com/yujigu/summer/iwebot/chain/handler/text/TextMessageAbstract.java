package com.yujigu.summer.iwebot.chain.handler.text;


import com.yujigu.summer.iwebot.chain.handler.MessageHandler;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.wechat.body.WechatTextMessage;

/**
 * 文本消息抽象请求类
 */
public abstract class TextMessageAbstract extends MessageHandler {

    public void setMessage(TextMessageAbstract china) {
        this.china = china;
    }

    protected abstract void execute(String receiver, WechatTextMessage wechatMessage);

    @Override
    protected void executeMessage(String receiver, WechatMessage wechatMessage) {
        execute(receiver, (WechatTextMessage) wechatMessage);
    }
}
