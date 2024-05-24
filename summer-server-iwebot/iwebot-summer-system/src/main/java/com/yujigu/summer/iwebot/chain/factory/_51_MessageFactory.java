package com.yujigu.summer.iwebot.chain.factory;


import com.yujigu.summer.iwebot.chain.message._51_Message;

public class _51_MessageFactory extends MessageFactory {

    @Override
    public Message createMessage() {
        return new _51_Message();
    }
}
