package com.yujigu.summer.iwebot.chain.factory;


import com.yujigu.summer.iwebot.chain.message._10000_Message;

public class _10000_MessageFactory extends MessageFactory {

    @Override
    public Message createMessage() {
        return new _10000_Message();
    }
}
