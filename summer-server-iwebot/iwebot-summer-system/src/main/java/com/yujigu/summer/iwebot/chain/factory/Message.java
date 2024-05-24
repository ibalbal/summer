package com.yujigu.summer.iwebot.chain.factory;

import com.yujigu.summer.iwebot.wechat.EnumsMsgType;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;

public interface Message {
    EnumsMsgType execute(String sender, String message);
}
