package com.yujigu.summer.iwebot.chain.handler.system;

import com.yujigu.summer.iwebot.wechat.body.WechatSystemMessage;

/**
 * 文本消息抽象请求类
 */
public abstract class SystemMessageAbstract {
    protected SystemMessageAbstract china;
    public void setMessage(SystemMessageAbstract china) {
        this.china = china;
    }
    public abstract void handleMessage(String receiver, WechatSystemMessage wechatSystemMessage);
}
