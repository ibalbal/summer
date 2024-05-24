package com.yujigu.summer.iwebot.chain.handler.text;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yujigu.summer.iwebot.entity.ResultMessageText;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.wechat.body.WechatTextMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

/**
 * 处理天气请求
 */
@Slf4j
@Component
public class TextWeatherHandler extends TextMessageAbstract{
    @Override
    public void handleMessage(String receiver, WechatTextMessage wechatMessage) {
        if (wechatMessage.content.endsWith("天气")){
            log.info("处理天气请求");
            String url = "https://wrest.rehi.org/weather";
            String city = wechatMessage.content.replaceAll("天气", "");
            if (ObjectUtils.isNotEmpty(city)){
                url = "https://wrest.rehi.org/weather/" + city;
            }
            String body = HttpUtil.createGet(url).execute().body();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            String resultMsg = jsonObject.getStr("text");
            ResultMessageText messageText = new ResultMessageText(resultMsg.replaceAll("；","\n"));
            messageText.setReceiver(receiver);
            messageText.execute();
        }else if(china  != null){
            china.handleMessage(receiver, wechatMessage);
        }

    }
}
