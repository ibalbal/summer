package com.yujigu.summer.iwebot.chain.factory;


import com.yujigu.summer.iwebot.chain.message._52_Message;

public class _52_MessageFactory extends MessageFactory {

    @Override
    public Message createMessage() {
        return new _52_Message();
    }
}
