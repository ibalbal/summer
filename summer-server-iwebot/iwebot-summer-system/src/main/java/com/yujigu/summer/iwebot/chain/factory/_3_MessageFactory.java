package com.yujigu.summer.iwebot.chain.factory;


import com.yujigu.summer.iwebot.chain.message._3_Message;

public class _3_MessageFactory extends MessageFactory {

    @Override
    public Message createMessage() {
        return new _3_Message();
    }
}
