package com.yujigu.summer.iwebot.chain.factory;


import com.yujigu.summer.iwebot.chain.message._1_Message;

public class _1_MessageFactory extends MessageFactory {

    @Override
    public Message createMessage() {
        return new _1_Message();
    }
}
