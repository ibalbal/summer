package com.yujigu.summer.iwebot.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.symxns.spring.log.annotation.SymLog;
import com.symxns.sym.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/message/api")
public class WeChatMessageController {

    @GetMapping
    @SymLog(sysModule = "微信接口",sysType = "微信接口",sysDesc = "微信接口")
    public Result api(@RequestParam("msg") String msg, @RequestParam(value = "receiver", defaultValue = "34550230053@chatroom") String receiver){

        if ("天气".contains(msg)){
            String url = "https://wrest.rehi.org/weather";
            String city = msg.replaceAll("天气", "");
            if (ObjectUtils.isNotEmpty(city)){
                url = "https://wrest.rehi.org/weather/" + city;
            }
            String body = HttpUtil.createGet(url).execute().body();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            String resultMsg = jsonObject.getStr("text");
            msg = resultMsg;
        }


        JSONObject param = JSONUtil.createObj();
        param.set("msg", msg);
        param.set("receiver", receiver);
        Map<String,String > headers = new HashMap();
        headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
        String body = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_txt").body(param.toString()).addHeaders(headers).execute().body();
        log.info(body);
        return Result.ok();
    }
}
