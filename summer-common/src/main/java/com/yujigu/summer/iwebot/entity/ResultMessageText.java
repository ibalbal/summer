package com.yujigu.summer.iwebot.entity;

import lombok.*;

import java.util.List;

/**
 * 发送文本消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultMessageText extends ResultMessage {
    //接收人
    private String receiver;
    //消息体
    private Object msg;

    //需要 @ 的用户 id 列表
    private List<String> aters;

    public ResultMessageText(String msg) {
        this.msg = msg;
    }

    @Override
    public String getSendUrl() {
        return "/wcf/send_txt";
    }
}
