package com.yujigu.summer.iwebot.wechat.factory;

import com.yujigu.summer.iwebot.wechat.EnumsMsgType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class _0_Message implements Message{
    @Override
    public EnumsMsgType execute() {
        log.info("执行0_Message");
        return EnumsMsgType._0;
    }
}
