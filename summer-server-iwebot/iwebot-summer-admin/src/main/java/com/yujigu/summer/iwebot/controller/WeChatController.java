package com.yujigu.summer.iwebot.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.symxns.spring.log.annotation.SymLog;
import com.symxns.sym.core.result.Result;
import com.yujigu.summer.iwebot.service.GcwService;
import com.yujigu.summer.iwebot.wechat.WechatMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("/callback/api")
public class WeChatController {

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
                gcw(wechatMessage.getContent(), receiver);
            }
            log.info("---：{}", wechatMessage);
        }catch (Exception e){
            log.info("e", e.getMessage());
        }
        return Result.ok();
    }

    public static void update(WechatMessage wechatMessage, String message, String receiver, boolean isGroup){
        if (wechatMessage.getSender().equals("wxid_oa0rwmnimagm21") && wechatMessage.getContent().equals("更新")){
            JSONObject webhookParams = JSONUtil.createObj();
            webhookParams.put("sender", receiver);
            String body = HttpUtil.createPost("http://flow-openapi.aliyun.com/pipeline/webhook/4TcgVAxeFsNWlrrjhLad").body(webhookParams.toString()).execute().body();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            Boolean successful = jsonObject.getBool("successful", false);

            JSONObject param = JSONUtil.createObj();

            String resultMsg;
            if (successful){
                resultMsg = "开始更新机器人后台...";
            }else {
                resultMsg ="更新错误："+ jsonObject.getStr("errorMsg");
            }
            param.set("msg", resultMsg);
            param.set("receiver", receiver);
            Map<String,String > headers = new HashMap();
            headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
            String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_txt").body(param.toString()).addHeaders(headers).execute().body();
            log.info(resultbody);
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
            JSONObject param = JSONUtil.createObj();
            param.set("msg", resultMsg.replaceAll("；","\n"));
            param.set("receiver", receiver);
            Map<String,String > headers = new HashMap();
            headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
            String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_txt").body(param.toString()).addHeaders(headers).execute().body();
            log.info(resultbody);
            return ;
        }

        if (message.startsWith("搜歌")){
            String url =  "http://mobilecdn.kugou.com/api/v3/search/song?format=json&keyword=SONGNAME&page=1&pagesize=1&showtype=1";
            String songName = message.replace("搜歌", "");
            String reqUrl = url.replace("SONGNAME", songName);
            String body = HttpUtil.createGet(reqUrl).execute().body();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            if (jsonObject.getInt("status") != 1) {
                JSONObject param = JSONUtil.createObj();
                param.set("msg", "获取数据失败"+jsonObject.getStr("error"));
                param.set("receiver", receiver);
                Map<String,String > headers = new HashMap();
                headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
                String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_txt").body(param.toString()).addHeaders(headers).execute().body();
                log.info(resultbody);
            }else {
                JSONObject data =  jsonObject.getJSONObject("data");
                JSONArray dataInfos = data.getJSONArray("info");
                JSONObject infosJSONObject = dataInfos.getJSONObject(0);
                if (ObjectUtils.isEmpty(infosJSONObject)){
                    JSONObject param = JSONUtil.createObj();
                    param.set("msg", "未搜到相关歌曲");
                    param.set("receiver", receiver);
                    Map<String,String > headers = new HashMap();
                    headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
                    String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_txt").body(param.toString()).addHeaders(headers).execute().body();
                    log.info(resultbody);
                    return;
                }

                String song_name = infosJSONObject.getStr("songname");
                String song_singer = infosJSONObject.getStr("singername");
                String quality = infosJSONObject.getStr("album_name");
                String cover = infosJSONObject.getStr("cover");
                String link = infosJSONObject.getStr("http://music.ibalbal.com/#/?type=kg&hash="+infosJSONObject.getStr("hash"));

                JSONObject param = JSONUtil.createObj();
                param.set("url", link);
                param.set("title", song_name );
                param.set("name",  quality);
                param.set("thumburl", cover);
                param.set("digest", song_singer);
                param.set("receiver", receiver);
                Map<String,String > headers = new HashMap();
                headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
                String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_rich_text").body(param.toString()).addHeaders(headers).execute().body();
                log.info(resultbody);
                return;
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
                    JSONObject param = JSONUtil.createObj();
                    param.set("msg", "未输入短剧名");
                    param.set("receiver", receiver);
                    Map<String,String > headers = new HashMap();
                    headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
                    String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_txt").body(param.toString()).addHeaders(headers).execute().body();
                    log.info(resultbody);
                    return;
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
            JSONObject param = JSONUtil.createObj();
            param.set("msg", resultMsg);
            param.set("receiver", receiver);
            Map<String,String > headers = new HashMap();
            headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
            String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_txt").body(param.toString()).addHeaders(headers).execute().body();
            log.info(resultbody);
            return;
        }


//
//        if (message.equals("黑丝")){
//            String url = "https://v2.api-m.com/api/heisi";
//            String body = HttpUtil.createGet(url).execute().body();
//            JSONObject jsonObject = JSONUtil.parseObj(body);
//            String resultMsg = jsonObject.getStr("data");
//            JSONObject param = JSONUtil.createObj();
//            param.set("path", resultMsg);
//            param.set("receiver", receiver);
//            Map<String,String > headers = new HashMap();
//            headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
//            String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_file").body(param.toString()).addHeaders(headers).execute().body();
//            log.info(resultbody);
//            return ;
//        }
//
//        if (message.equals("白丝")){
//            String url = "https://v2.api-m.com/api/baisi";
//            String body = HttpUtil.createGet(url).execute().body();
//            JSONObject jsonObject = JSONUtil.parseObj(body);
//            String resultMsg = jsonObject.getStr("data");
//            JSONObject param = JSONUtil.createObj();
//            param.set("path", resultMsg);
//            param.set("receiver", receiver);
//            Map<String,String > headers = new HashMap();
//            headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
//            String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_file").body(param.toString()).addHeaders(headers).execute().body();
//            log.info(resultbody);
//            return ;
//        }
//
//
//        if (message.equals("小姐姐视频")){
//            String url = "www.wudada.online/Api/NewSp";
//            String resultMsg =  HttpUtil.createGet(url).setFollowRedirects(false).executeAsync().header("Location");
//            JSONObject param = JSONUtil.createObj();
//            param.set("path", resultMsg);
//            param.set("receiver", receiver);
//            Map<String,String > headers = new HashMap();
//            headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
//            String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_file").body(param.toString()).addHeaders(headers).execute().body();
//            log.info(resultbody);
//            return ;
//        }
//
//        if (message.equals("占卜")){
//            String url = "https://www.hhlqilongzhu.cn/api/tu_yunshi.php";
//            JSONObject param = JSONUtil.createObj();
//            param.set("path", url);
//            param.set("receiver", receiver);
//            Map<String,String > headers = new HashMap();
//            headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
//            String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_file").body(param.toString()).addHeaders(headers).execute().body();
//            log.info(resultbody);
//            return ;
//        }


    }


    public void gcw(String message, String receiver){
        if (message.startsWith("搜舞")){
            StringBuilder msg = new StringBuilder();

            String data = message.replaceAll("搜舞", "").trim();

            if (ObjectUtils.isNotEmpty(data)) {
                msg.append( gcwService.gcw(data));
            }else {
                msg.append("未输入歌曲名");
            }

            JSONObject param = JSONUtil.createObj();
            param.set("msg", msg);
            param.set("receiver", receiver);
            Map<String,String > headers = new HashMap();
            headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
            String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_txt").body(param.toString()).addHeaders(headers).execute().body();
            log.info(resultbody);
            return ;
        }
        if (message.startsWith("下舞")){
            StringBuilder msg = new StringBuilder();
            String data = message.replaceAll("下舞", "").trim();
            if (ObjectUtils.isNotEmpty(data)) {
                msg.append(gcwService.gcwMp3(data));
            }else {
                msg.append("未输入歌曲链接");
            }

            JSONObject param = JSONUtil.createObj();
            param.set("msg", msg);
            param.set("receiver", receiver);
            Map<String,String > headers = new HashMap();
            headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
            String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_txt").body(param.toString()).addHeaders(headers).execute().body();
            log.info(resultbody);
            return ;
        }
    }



}
