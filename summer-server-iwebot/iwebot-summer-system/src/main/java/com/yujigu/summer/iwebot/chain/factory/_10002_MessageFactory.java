package com.yujigu.summer.iwebot.chain.factory;


import com.yujigu.summer.iwebot.chain.message._10002_Message;

public class _10002_MessageFactory extends MessageFactory {

    @Override
    public Message createMessage() {
        return new _10002_Message();
    }
}
