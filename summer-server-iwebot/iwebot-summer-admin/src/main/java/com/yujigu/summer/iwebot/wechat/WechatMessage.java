package com.yujigu.summer.iwebot.wechat;

import lombok.Data;

/**
 * @Author ly
 * @Date 2024/3/19 22:31
 * @Description 微信消息
 */
@Data
public class WechatMessage {
    public String content;
    public Long id;
    public String sender;
    public String sign;
    public Long ts;
    public Long type;
    public Xml xml;
}
