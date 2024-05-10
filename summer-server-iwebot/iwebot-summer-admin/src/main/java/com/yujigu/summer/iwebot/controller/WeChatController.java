package com.yujigu.summer.iwebot.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONGetter;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.symxns.spring.log.annotation.SymLog;
import com.symxns.sym.core.result.Result;
import com.yujigu.summer.iwebot.entity.MessageRichText;
import com.yujigu.summer.iwebot.entity.MessageText;
import com.yujigu.summer.iwebot.service.GcwService;
import com.yujigu.summer.iwebot.wechat.WechatMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author ly
 * @Date 2024/3/19 21:51
 * @Description wechat api
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/callback/api")
public class WeChatController {

    @Value("${iwebot.welcome:'欢迎进群聊，请认真看公告'}")
    private String welcomeMessage;

    @Autowired
    private GcwService gcwService;

    @PostMapping
    @SymLog(sysModule = "微信接口",sysType = "微信接口",sysDesc = "微信接口", result = false)
    public Result api(@RequestBody String message){
        log.info("api message:{}", message);

        try{
            WechatMessage wechatMessage = com.alibaba.fastjson2.JSONObject.parseObject(message, WechatMessage.class);
            if (wechatMessage.getType() == 1){
                String receiver = wechatMessage.Group ? wechatMessage.getRoomid() : wechatMessage.getSender();
                update(wechatMessage, wechatMessage.getContent(), receiver, wechatMessage.Group);

                cover(wechatMessage.getContent(), receiver);
            }

            if (wechatMessage.getType() == 10000){
                welcome(welcomeMessage, wechatMessage.getRoomid());
            }
            log.info("---：{}", wechatMessage);
        }catch (Exception e){
            log.info("e", e.getMessage());
        }
        return Result.ok();
    }

    public static void welcome(String message, String receiver){
//        MessageText messageText = new MessageText(message);
//        messageText.setReceiver(receiver);
//        messageText.execute();
    }

    public static void update(WechatMessage wechatMessage, String message, String receiver, boolean isGroup){
        if (wechatMessage.getSender().equals("wxid_oa0rwmnimagm21") && wechatMessage.getContent().equals("更新")){
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
            MessageText messageText = new MessageText(resultMsg);
            messageText.setReceiver(receiver);
            messageText.execute();
        }
    }

    public static void cover(String message, String receiver){
        if (message.endsWith("天气")){
            String url = "https://wrest.rehi.org/weather";
            String city = message.replaceAll("天气", "");
            if (ObjectUtils.isNotEmpty(city)){
                url = "https://wrest.rehi.org/weather/" + city;
            }
            String body = HttpUtil.createGet(url).execute().body();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            String resultMsg = jsonObject.getStr("text");
            MessageText messageText = new MessageText(resultMsg.replaceAll("；","\n"));
            messageText.setReceiver(receiver);
            messageText.execute();
            return ;
        }

        if (message.startsWith("点歌")){
            String url =  "http://mobilecdn.kugou.com/api/v3/search/song?format=json&keyword=SONGNAME&page=1&pagesize=1&showtype=1";
            String songName = message.replace("点歌", "");
            String reqUrl = url.replace("SONGNAME", songName);
            String body = HttpUtil.createGet(reqUrl).execute().body();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            if (jsonObject.getInt("status") != 1) {
                MessageText messageText = new MessageText();
                messageText.setMsg("获取数据失败"+jsonObject.getStr("error"));
                messageText.setReceiver(receiver);
                messageText.execute();
            }else {
                JSONObject data =  jsonObject.getJSONObject("data");
                JSONArray dataInfos = data.getJSONArray("info");
                JSONObject infosJSONObject = dataInfos.getJSONObject(0);
                if (ObjectUtils.isEmpty(infosJSONObject)){
                    MessageText messageText = new MessageText();
                    messageText.setMsg("未搜到相关歌曲");
                    messageText.setReceiver(receiver);
                    messageText.execute();
                }
                String song_name = infosJSONObject.getStr("songname");
                String song_singer = infosJSONObject.getStr("singername");
                String quality = "即音乐";
                String cover = "https://minio.ibalbal.com:800/wx-mini/music.png";
                String link = "http://music.ibalbal.com/#/pages/play/music?type=kg&hash="+ infosJSONObject.getStr("hash");
                MessageRichText messageText = new MessageRichText();
                messageText.setUrl(link);
                messageText.setTitle( song_name );
                messageText.setName( quality);
                messageText.setThumburl(cover);
                messageText.setDigest(song_singer);
                messageText.setReceiver(receiver);
                messageText.execute();
            }
        }

        if (message.startsWith("短剧")){
            StringBuilder resultMsg = new StringBuilder();
            try {
                String url = "https://api.djcat.sbs/api/movies?page=1&limit=5&name=NAME";
                String data = message.replaceAll("短剧", "").trim();
                String newUrl = null;
                if (ObjectUtils.isNotEmpty(data)) {
                    newUrl = url.replace("NAME", data);
                }else {
                    MessageText messageText = new MessageText();
                    messageText.setMsg("未输入短剧名");
                    messageText.setReceiver(receiver);
                    messageText.execute();
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
            MessageText messageText = new MessageText();
            messageText.setMsg(resultMsg);
            messageText.setReceiver(receiver);
            messageText.execute();
        }

        if (message.startsWith("小说")){
            StringBuilder resultMsg = new StringBuilder();
            try {
                String data = message.replaceAll("小说", "").trim();

                String[] content = data.split("\\s+");
                if (content.length<=1){
                    String url = "https://www.hhlqilongzhu.cn/api/novel_1.php?name=NAME&type=json";
                    String newUrl = null;
                    if (ObjectUtils.isNotEmpty(data)) {
                        newUrl = url.replace("NAME", data);
                    }else {
                        MessageText messageText = new MessageText();
                        messageText.setMsg("未输入小说名");
                        messageText.setReceiver(receiver);
                        messageText.execute();
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
            MessageText messageText = new MessageText();
            messageText.setMsg(resultMsg);
            messageText.setReceiver(receiver);
            messageText.execute();
        }
    }
}
