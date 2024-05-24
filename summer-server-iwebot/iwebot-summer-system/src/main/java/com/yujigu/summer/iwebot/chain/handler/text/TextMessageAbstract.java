package com.yujigu.summer.iwebot.chain.handler.text;


import com.yujigu.summer.iwebot.wechat.body.WechatTextMessage;

/**
 * 文本消息抽象请求类
 */
public abstract class TextMessageAbstract {
    protected TextMessageAbstract china;
    public void setMessage(TextMessageAbstract china) {
        this.china = china;
    }
    public abstract void handleMessage(String receiver, WechatTextMessage wechatMessage);
}
