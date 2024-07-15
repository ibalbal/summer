package com.yujigu.summer.iwebot.entity;

import lombok.*;

/**
 * 发送图片消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultMessageImage extends ResultMessage {
    //图片地址
    private String path;
    //接收人
    @Setter
    @Getter
    private String receiver;
    @Override
    public String getSendUrl() {
        return "/wcf/send_img";
    }
}
