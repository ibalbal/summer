package com.yujigu.summer.iwebot.wechat.factory;

import com.yujigu.summer.iwebot.wechat.EnumsMsgType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class _1_Message implements Message{
    @Override
    public EnumsMsgType execute() {
        log.info("执行1_Message");
        return EnumsMsgType._1;
    }
}
