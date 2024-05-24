package com.yujigu.summer.iwebot.wechat.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author ly
 * @Date 2024/3/19 22:31
 * @Description 微信消息
 */
public class WechatMessage {
    @Getter
    @Setter
    public Long id;

    @Getter
    @Setter
    public String sender;

    @Getter
    @Setter
    public String sign;

    @Getter
    @Setter
    public Long ts;

    @Getter
    @Setter
    public String type;

    @Getter
    @Setter
    @JsonProperty("is_group")
    public boolean Group;

    @Getter
    @Setter
    public String roomid;

}
