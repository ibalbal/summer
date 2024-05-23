package com.yujigu.summer.iwebot.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    public String type;
    public Xml xml;

    @JsonProperty("is_group")
    public boolean Group;

    public String roomid;

    public WechatMessage(String type) {
        this.type = type;
    }
}
