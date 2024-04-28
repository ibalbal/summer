package com.yujigu.summer.iwebot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送文件信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageFile extends Message{
    //文件地址
    private String path;

    @Override
    public String getSendUrl() {
        return "/wcf/send_file";
    }
}
