package com.yujigu.summer.iwebot.chain.handler.text;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yujigu.summer.iwebot.entity.ResultMessageText;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.wechat.body.WechatTextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 处理系统更新请求
 */
@Slf4j
@Component
public class TextUpdateSystemHandler extends TextMessageAbstract{
    @Override
    public void execute(String receiver, WechatTextMessage wechatMessage) {
        log.info("处理系统更新");
        JSONObject webhookParams = JSONUtil.createObj();
        webhookParams.put("sender", receiver);
        String body = HttpUtil.createPost("http://flow-openapi.aliyun.com/pipeline/webhook/4TcgVAxeFsNWlrrjhLad").body(webhookParams.toString()).execute().body();
        JSONObject jsonObject = JSONUtil.parseObj(body);
        Boolean successful = jsonObject.getBool("successful", false);

        String resultMsg;
        if (successful){
            resultMsg = "开始更新机器人后台...";
        }else {
            resultMsg ="更新错误："+ jsonObject.getStr("errorMsg");
        }
        ResultMessageText messageText = new ResultMessageText(resultMsg);
        messageText.setReceiver(receiver);
        messageText.execute();
    }

    @Override
    public boolean isChain(WechatMessage wechatMessage) {
        WechatTextMessage wechatTextMessage = (WechatTextMessage) wechatMessage;
        return wechatTextMessage.getSender().equals("wxid_oa0rwmnimagm21") && wechatTextMessage.getContent().equals("更新");
    }
}
