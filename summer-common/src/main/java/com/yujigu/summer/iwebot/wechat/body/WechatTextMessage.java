package com.yujigu.summer.iwebot.wechat.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author ly
 * @Description 微信消息
 */
@Data
public class WechatTextMessage extends WechatMessage{
    public String content;
    public Xml xml;
}
