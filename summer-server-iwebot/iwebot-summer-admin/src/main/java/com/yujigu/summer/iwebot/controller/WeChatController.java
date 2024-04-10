package com.yujigu.summer.iwebot.controller;

import cn.hutool.http.HttpUtil;
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

import java.util.Arrays;
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
                cover(wechatMessage.getContent(), receiver);
            }
            log.info("---：{}", wechatMessage);
        }catch (Exception e){
            log.info("e", e.getMessage());
        }
        return Result.ok();
    }

    public static void cover(String message, String receiver){
        if (message.contains("天气")){
            String url = "https://wrest.rehi.org/weather";
            String city = message.replaceAll("天气", "");
            if (ObjectUtils.isNotEmpty(city)){
                url = "https://wrest.rehi.org/weather/" + city;
            }
            String body = HttpUtil.createGet(url).execute().body();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            String resultMsg = jsonObject.getStr("text");
            JSONObject param = JSONUtil.createObj();
            param.set("msg", resultMsg);
            param.set("receiver", receiver);
            Map<String,String > headers = new HashMap();
            headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
            String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_txt").body(param.toString()).addHeaders(headers).execute().body();
            log.info(resultbody);
            return ;
        }

        if (message.equals("黑丝")){
            String url = "https://v2.api-m.com/api/heisi";
            String body = HttpUtil.createGet(url).execute().body();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            String resultMsg = jsonObject.getStr("data");
            JSONObject param = JSONUtil.createObj();
            param.set("path", resultMsg);
            param.set("receiver", receiver);
            Map<String,String > headers = new HashMap();
            headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
            String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_file").body(param.toString()).addHeaders(headers).execute().body();
            log.info(resultbody);
            return ;
        }

        if (message.equals("白丝")){
            String url = "https://v2.api-m.com/api/baisi";
            String body = HttpUtil.createGet(url).execute().body();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            String resultMsg = jsonObject.getStr("data");
            JSONObject param = JSONUtil.createObj();
            param.set("path", resultMsg);
            param.set("receiver", receiver);
            Map<String,String > headers = new HashMap();
            headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
            String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_file").body(param.toString()).addHeaders(headers).execute().body();
            log.info(resultbody);
            return ;
        }


        if (message.equals("小姐姐视频")){
            String url = "www.wudada.online/Api/NewSp";
            String resultMsg =  HttpUtil.createGet(url).setFollowRedirects(false).executeAsync().header("Location");
            JSONObject param = JSONUtil.createObj();
            param.set("path", resultMsg);
            param.set("receiver", receiver);
            Map<String,String > headers = new HashMap();
            headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
            String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_file").body(param.toString()).addHeaders(headers).execute().body();
            log.info(resultbody);
            return ;
        }

        if (message.equals("占卜")){
            String url = "https://www.hhlqilongzhu.cn/api/tu_yunshi.php";
            JSONObject param = JSONUtil.createObj();
            param.set("path", url);
            param.set("receiver", receiver);
            Map<String,String > headers = new HashMap();
            headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
            String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_file").body(param.toString()).addHeaders(headers).execute().body();
            log.info(resultbody);
            return ;
        }

    }
}
