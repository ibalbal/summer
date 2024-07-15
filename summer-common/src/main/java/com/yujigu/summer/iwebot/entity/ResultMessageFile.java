package com.yujigu.summer.iwebot.entity;

import lombok.*;

/**
 * 发送文件信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultMessageFile extends ResultMessage {
    //文件地址
    private String path;
    //接收人
    private String receiver;
    @Override
    public String getSendUrl() {
        return "/wcf/send_file";
    }
}
