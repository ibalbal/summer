package com.yujigu.summer.iwebot.chain.factory;


import com.yujigu.summer.iwebot.chain.message._53_Message;

public class _53_MessageFactory extends MessageFactory {

    @Override
    public Message createMessage() {
        return new _53_Message();
    }
}
