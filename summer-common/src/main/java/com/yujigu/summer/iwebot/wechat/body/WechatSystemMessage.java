package com.yujigu.summer.iwebot.wechat.body;

import lombok.Data;

/**
 * @Author ly
 * @Description 微信消息
 */
@Data
public class WechatSystemMessage extends WechatMessage{
    public String content;
    public String xml;
}
