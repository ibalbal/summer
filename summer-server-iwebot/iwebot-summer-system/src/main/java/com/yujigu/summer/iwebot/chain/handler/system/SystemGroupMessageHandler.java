package com.yujigu.summer.iwebot.chain.handler.system;

import com.yujigu.summer.iwebot.entity.ResultMessageText;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.wechat.body.WechatSystemMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class SystemGroupMessageHandler extends SystemMessageAbstract{

    @Override
    protected void execute(String receiver, WechatSystemMessage wechatSystemMessage) {
        if (wechatSystemMessage.content.contains("修改群名为")){
            ResultMessageText messageText = new ResultMessageText(wechatSystemMessage.content);
            messageText.setReceiver(receiver);
            messageText.execute();
        }else if (wechatSystemMessage.content.contains("加入了群聊")){
            // 定义正则表达式，匹配邀请者和被邀请者
            String regex = "(\\S+)邀请(\\S+)加入了群聊";
            // 编译正则表达式
            Pattern pattern = Pattern.compile(regex);
            // 创建匹配器
            Matcher matcher = pattern.matcher(wechatSystemMessage.content);
            // 检查是否匹配
            if (matcher.find()) {
                // 提取匹配的组
                String inviter = matcher.group(1); // "邀请者"
                String invitee = matcher.group(2); // "被邀请者"
                ResultMessageText messageText = new ResultMessageText("欢迎"+invitee+"加入群聊");
                messageText.setReceiver(receiver);
                messageText.execute();
            } else {
                ResultMessageText messageText = new ResultMessageText("欢迎加入群聊");
                messageText.setReceiver(receiver);
                messageText.execute();
            }
        }else if (china  != null){
            china.handleMessage(receiver, wechatSystemMessage);
        }
    }

    @Override
    public boolean isChain(WechatMessage wechatMessage) {
        return true;
    }
}
