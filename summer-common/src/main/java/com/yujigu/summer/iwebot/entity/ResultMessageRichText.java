package com.yujigu.summer.iwebot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送卡片 文本消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultMessageRichText extends ResultMessage {

    //填公众号 id 可以显示对应的头像（gh_ 开头的）
    private String account;

    //摘要，三行
    private String digest;

    //左下显示的名字
    private String name;

    //缩略图的链接
    private String thumburl;

    //标题，最多两行
    private String title;

    //点击后跳转的链接
    private String url;

    @Override
    public String getSendUrl() {
        return "/wcf/send_rich_text";
    }
}
