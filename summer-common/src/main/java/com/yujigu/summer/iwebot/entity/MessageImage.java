package com.yujigu.summer.iwebot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送图片消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageImage extends Message{
    //图片地址
    private String path;

    @Override
    public String getSendUrl() {
        return "/wcf/send_img";
    }
}
