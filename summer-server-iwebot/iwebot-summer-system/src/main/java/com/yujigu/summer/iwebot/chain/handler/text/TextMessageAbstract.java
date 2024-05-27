package com.yujigu.summer.iwebot.chain.handler.text;


import com.yujigu.summer.iwebot.chain.handler.MessageHandler;
import com.yujigu.summer.iwebot.entity.ResultMessage;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.wechat.body.WechatTextMessage;

/**
 * 文本消息抽象请求类
 */
public abstract class TextMessageAbstract extends MessageHandler {

    public void setMessage(TextMessageAbstract china) {
        this.china = china;
    }

    protected abstract ResultMessage execute(String receiver, WechatTextMessage wechatMessage);

    @Override
    protected ResultMessage executeMessage(String receiver, WechatMessage wechatMessage) {
        return execute(receiver, (WechatTextMessage) wechatMessage);
    }
}
