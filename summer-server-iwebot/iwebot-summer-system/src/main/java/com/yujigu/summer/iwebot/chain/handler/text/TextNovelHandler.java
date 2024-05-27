package com.yujigu.summer.iwebot.chain.handler.text;

import cn.hutool.http.HttpUtil;
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
 * 小说
 */
@Slf4j
@Component
public class TextNovelHandler extends TextMessageAbstract{
    @Override
    public ResultMessage execute(String receiver, WechatTextMessage wechatMessage) {

        log.info("小说");
        StringBuilder resultMsg = new StringBuilder();
        try {
            String data = wechatMessage.content.replaceAll("小说", "").trim();

            String[] content = data.split("\\s+");
            if (content.length<=1){
                String url = "https://www.hhlqilongzhu.cn/api/novel_1.php?name=NAME&type=json";
                String newUrl = null;
                if (ObjectUtils.isNotEmpty(data)) {
                    newUrl = url.replace("NAME", data);
                }else {
                    ResultMessageText messageText = new ResultMessageText();
                    messageText.setMsg("未输入小说名");
                    messageText.setReceiver(receiver);
                    return messageText;
                }
                String body = HttpUtil.createGet(newUrl).execute().body();
                if (ObjectUtils.isEmpty(body)) {
                    resultMsg = new StringBuilder("未找到资源");
                } else {
                    resultMsg.append("温馨提示：");
                    resultMsg.append("\n");
                    resultMsg.append("所有资源来源于网络，侵删");
                    resultMsg.append("\n");
                    resultMsg.append("---------------------");
                    resultMsg.append("\n");
                    resultMsg.append(body);
                }
            }else {
                String name = content[0];
                String n = content[1];
                String url = "https://www.hhlqilongzhu.cn/api/novel_1.php?name=NAME&type=json&n="+n;
                String newUrl = url.replace("NAME", name);
                String body = HttpUtil.createGet(newUrl).execute().body();
                JSONObject jsonObject = JSONUtil.parseObj(body);
                if (ObjectUtils.isEmpty(jsonObject.get("download"))) {
                    resultMsg = new StringBuilder("数据为空");
                } else {
                    resultMsg.append("温馨提示：");
                    resultMsg.append("\n");
                    resultMsg.append("所有资源来源于网络，侵删");
                    resultMsg.append("\n");
                    resultMsg.append("---------------------");
                    resultMsg.append("\n");
                    resultMsg.append("书名：");
                    resultMsg.append(jsonObject.getStr("title"));
                    resultMsg.append("\n");
                    resultMsg.append("作者：");
                    resultMsg.append(jsonObject.getStr("author"));
                    resultMsg.append("\n");
                    resultMsg.append("类型：");
                    resultMsg.append(jsonObject.getStr("type"));
                    resultMsg.append("\n");
                    resultMsg.append("下载链接：");
                    resultMsg.append(jsonObject.getStr("download"));
                    resultMsg.append("\n");
                    resultMsg.append("\n");
                    resultMsg.append("简介：").append(jsonObject.getStr("js"));
                    resultMsg.append("\n");
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
        return wechatTextMessage.content.startsWith("小说");
    }
}
