package com.yujigu.summer.iwebot.chain.factory;


import com.yujigu.summer.iwebot.chain.message._50_Message;

public class _50_MessageFactory extends MessageFactory {

    @Override
    public Message createMessage() {
        return new _50_Message();
    }
}
