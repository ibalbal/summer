package com.yujigu.summer.iwebot.chain.handler.text;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yujigu.summer.iwebot.entity.ResultMessageRichText;
import com.yujigu.summer.iwebot.entity.ResultMessageText;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.wechat.body.WechatTextMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TextSongHandler extends TextMessageAbstract{
    @Override
    public void execute(String receiver, WechatTextMessage wechatMessage) {
        log.info("点歌");
        String url =  "http://mobilecdn.kugou.com/api/v3/search/song?format=json&keyword=SONGNAME&page=1&pagesize=1&showtype=1";
        String songName = wechatMessage.content.replace("点歌", "");
        String reqUrl = url.replace("SONGNAME", songName);
        String body = HttpUtil.createGet(reqUrl).execute().body();
        JSONObject jsonObject = JSONUtil.parseObj(body);
        if (jsonObject.getInt("status") != 1) {
            ResultMessageText messageText = new ResultMessageText();
            messageText.setMsg("获取数据失败"+jsonObject.getStr("error"));
            messageText.setReceiver(receiver);
            messageText.execute();
        }else {
            JSONObject data =  jsonObject.getJSONObject("data");
            JSONArray dataInfos = data.getJSONArray("info");
            JSONObject infosJSONObject = dataInfos.getJSONObject(0);
            if (ObjectUtils.isEmpty(infosJSONObject)){
                ResultMessageText messageText = new ResultMessageText();
                messageText.setMsg("未搜到相关歌曲");
                messageText.setReceiver(receiver);
                messageText.execute();
            }
            String song_name = infosJSONObject.getStr("songname");
            String song_singer = infosJSONObject.getStr("singername");
            String quality = "即音乐";
            String cover = "https://minio.ibalbal.com:800/wx-mini/music.png";
            String link = "http://music.ibalbal.com/#/pages/play/music?type=kg&hash="+ infosJSONObject.getStr("hash");
            ResultMessageRichText messageText = new ResultMessageRichText();
            messageText.setUrl(link);
            messageText.setTitle( song_name );
            messageText.setName( quality);
            messageText.setThumburl(cover);
            messageText.setDigest(song_singer);
            messageText.setReceiver(receiver);
            messageText.execute();
        }
    }

    @Override
    public boolean isChain(WechatMessage wechatMessage) {
        WechatTextMessage wechatTextMessage = (WechatTextMessage) wechatMessage;
        return wechatTextMessage.content.startsWith("点歌");
    }
}
