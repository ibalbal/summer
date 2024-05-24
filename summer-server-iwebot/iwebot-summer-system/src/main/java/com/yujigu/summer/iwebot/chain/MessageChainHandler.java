package com.yujigu.summer.iwebot.chain;

import com.yujigu.summer.iwebot.wechat.body.WechatMessage;

public interface MessageChainHandler {
    void handleMessage(String sender, WechatMessage wechatMessage);
}
