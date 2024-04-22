package com.yujigu.summer.iwebot.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.symxns.spring.log.annotation.SymLog;
import com.symxns.sym.core.result.Result;
import com.yujigu.summer.iwebot.wechat.WechatMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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

        if (message.contains("搜歌")){
            String url =  "https://www.hhlqilongzhu.cn/api/dg_qqmusic_SQ.php?num=10&msg=SONGNAME&n=NO&type=TYPE";
            String[] content = message.split("\\s+");
            String songName = content[1];
            if (content.length > 2){
                String no = content[2];
                String reqUrl = url.replace("SONGNAME", songName).replace("NO", no).replace("TYPE", "json");
                String body = HttpUtil.createGet(reqUrl).execute().body();
                JSONObject jsonObject = JSONUtil.parseObj(body);
                if (jsonObject.getInt("code") != 200) {
                    JSONObject param = JSONUtil.createObj();
                    param.set("msg", "获取数据失败");
                    param.set("receiver", receiver);
                    Map<String,String > headers = new HashMap();
                    headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
                    String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_txt").body(param.toString()).addHeaders(headers).execute().body();
                    log.info(resultbody);
                }else {
                    JSONObject data =  jsonObject.getJSONObject("data");
                    String song_name = data.getStr("song_name");
                    String song_singer = data.getStr("song_singer");
                    String quality = data.getStr("quality");
                    String cover = data.getStr("cover");
                    String link = data.getStr("link");
                    String music_url = data.getStr("music_url");

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
                }

                return;
            }else {
                String reqUrl = url.replace("SONGNAME", songName).replace("NO", "").replace("TYPE", "");
                String resultMsg = HttpUtil.createGet(reqUrl).execute().body();
                resultMsg += "\n\n";

                resultMsg += "选择指定歌曲：搜歌 "+songName+" " + "1";
                resultMsg += "\n";
                resultMsg += "其中1为歌曲序号";
                JSONObject param = JSONUtil.createObj();
                param.set("msg", resultMsg);
                param.set("receiver", receiver);
                Map<String,String > headers = new HashMap();
                headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
                String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_txt").body(param.toString()).addHeaders(headers).execute().body();
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





}
