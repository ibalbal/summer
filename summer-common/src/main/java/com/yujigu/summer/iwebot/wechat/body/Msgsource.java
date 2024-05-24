package com.yujigu.summer.iwebot.wechat.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author ly
 * @Date 2024/3/19 22:32
 * @Description wechat message xml msgsource
 */
@Data
public class Msgsource {
    public String bizflag;
    public String pua;
    public String signature;

    @JsonProperty("tmp_node")
    public TmpNode tmpNode;
}
