package com.yujigu.summer.iwebot.chain.factory;


import com.yujigu.summer.iwebot.chain.message._42_Message;

public class _42_MessageFactory extends MessageFactory {

    @Override
    public Message createMessage() {
        return new _42_Message();
    }
}
