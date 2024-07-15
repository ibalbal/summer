package com.yujigu.summer.iwebot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 转发消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultMessageForward extends ResultMessage {

    //消息id
    private Long id;

    //需要 @ 的用户 id 列表
    private List<String> receiver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<String> receiver) {
        this.receiver = receiver;
    }

    @Override
    public String getSendUrl() {
        return "/wcf/forward_msg";
    }
}
