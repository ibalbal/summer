package com.yujigu.summer.iwebot.chain.handler.text;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yujigu.summer.iwebot.entity.ResultMessage;
import com.yujigu.summer.iwebot.entity.ResultMessageText;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.wechat.body.WechatTextMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

/**
 * 短剧
 */
@Slf4j
@Component
public class TextShortVideoHandler extends TextMessageAbstract{
    @Override
    public ResultMessage execute(String receiver, WechatTextMessage wechatMessage) {
        log.info("短剧");
        StringBuilder resultMsg = new StringBuilder();
        try {
            String url = "https://api.djcat.sbs/api/movies?page=1&limit=5&name=NAME";
            String data = wechatMessage.content.replaceAll("短剧", "").trim();
            String newUrl = null;
            if (ObjectUtils.isNotEmpty(data)) {
                newUrl = url.replace("NAME", data);
            }else {
                ResultMessageText messageText = new ResultMessageText();
                messageText.setMsg("未输入短剧名");
                messageText.setReceiver(receiver);
                return messageText;
            }
            String body = HttpUtil.createGet(newUrl).execute().body();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            Integer code = jsonObject.getInt("code");
            if (code != 0) {
                resultMsg = new StringBuilder(jsonObject.getStr("message"));
            } else {
                resultMsg.append("温馨提示：");
                resultMsg.append("\n");
                resultMsg.append("所有资源来源于网络，侵删");
                resultMsg.append("\n");
                resultMsg.append("---------------------");
                resultMsg.append("\n");

                JSONObject moviesData = jsonObject.getJSONObject("data");
                if (moviesData.getInt("total") == 0) {
                    resultMsg = new StringBuilder("没有找到相关短剧");
                } else {
                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("rows");

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        resultMsg.append(jsonObject1.getStr("name"));
                        resultMsg.append("\n");
                        resultMsg.append("链接：").append(jsonObject1.getStr("link"));
                        resultMsg.append("\n\n");
                    }
                }
            }
        }catch (Exception e){
            resultMsg = new StringBuilder("系统异常");
        }
        ResultMessageText messageText = new ResultMessageText();
        messageText.setMsg(resultMsg);
        messageText.setReceiver(receiver);
        return messageText;
    }

    @Override
    public boolean isChain(WechatMessage wechatMessage) {
        WechatTextMessage wechatTextMessage = (WechatTextMessage) wechatMessage;
        return wechatTextMessage.content.startsWith("短剧");
    }
}
