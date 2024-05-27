package com.yujigu.summer.iwebot.chain.handler.text;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yujigu.summer.iwebot.entity.ResultMessage;
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
    public ResultMessage execute(String receiver, WechatTextMessage wechatMessage) {

        log.info("点歌");
        String url =  "http://mobilecdn.kugou.com/api/v3/search/song?format=json&keyword=SONGNAME&page=1&pagesize=1&showtype=1";
        String songName = wechatMessage.content.replace("点歌", "");
        String reqUrl = url.replace("SONGNAME", songName);
        String body = HttpUtil.createGet(reqUrl).execute().body();
        JSONObject jsonObject = JSONUtil.parseObj(body);
        ResultMessageText messageText = new ResultMessageText();

        if (jsonObject.getInt("status") != 1) {
            messageText.setMsg("获取数据失败"+jsonObject.getStr("error"));
            messageText.setReceiver(receiver);
            return messageText;
        }else {
            JSONObject data =  jsonObject.getJSONObject("data");
            JSONArray dataInfos = data.getJSONArray("info");
            JSONObject infosJSONObject = dataInfos.getJSONObject(0);
            if (ObjectUtils.isEmpty(infosJSONObject)){
                messageText.setMsg("未搜到相关歌曲");
                messageText.setReceiver(receiver);
                return messageText;
            }
            String song_name = infosJSONObject.getStr("songname");
            String song_singer = infosJSONObject.getStr("singername");
            String quality = "即音乐";
            String cover = "https://minio.ibalbal.com:800/wx-mini/music.png";
            String link = "http://music.ibalbal.com/#/pages/play/music?type=kg&hash="+ infosJSONObject.getStr("hash");
            ResultMessageRichText resultMessageRichText = new ResultMessageRichText();
            resultMessageRichText.setUrl(link);
            resultMessageRichText.setTitle( song_name );
            resultMessageRichText.setName( quality);
            resultMessageRichText.setThumburl(cover);
            resultMessageRichText.setDigest(song_singer);
            resultMessageRichText.setReceiver(receiver);
            return resultMessageRichText;
        }
    }

    @Override
    public boolean isChain(WechatMessage wechatMessage) {
        WechatTextMessage wechatTextMessage = (WechatTextMessage) wechatMessage;
        return wechatTextMessage.content.startsWith("点歌");
    }
}
