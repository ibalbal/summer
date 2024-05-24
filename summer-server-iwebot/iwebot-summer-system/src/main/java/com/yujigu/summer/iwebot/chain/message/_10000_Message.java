package com.yujigu.summer.iwebot.chain.message;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import com.yujigu.summer.iwebot.chain.SystemMessageChainHandler;
import com.yujigu.summer.iwebot.chain.TextMessageChainHandler;
import com.yujigu.summer.iwebot.wechat.EnumsMsgType;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.chain.factory.Message;
import com.yujigu.summer.iwebot.wechat.body.WechatSystemMessage;
import com.yujigu.summer.iwebot.wechat.body.WechatTextMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class _10000_Message implements Message {
    @Override
    public EnumsMsgType execute(String receiver, String message) {
        log.info("执行10000_Message");
        SystemMessageChainHandler chainHandler = SpringUtil.getBean(SystemMessageChainHandler.class);
        WechatMessage wechatMessage = JSONObject.parseObject(message, WechatSystemMessage.class);
        chainHandler.handleMessage(receiver, wechatMessage);
        return EnumsMsgType._10000;
    }
}
