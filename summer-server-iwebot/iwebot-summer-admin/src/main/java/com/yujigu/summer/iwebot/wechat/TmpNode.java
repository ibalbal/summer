package com.yujigu.summer.iwebot.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author ly
 * @Date 2024/3/19 22:33
 * @Description TmpNode
 */
@Data
public class TmpNode {
    @JsonProperty("publisher-id")
    public String publisherId;
}
