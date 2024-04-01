package com.yujigu.summer.iwebot.controller;

import com.symxns.spring.log.annotation.SymLog;
import com.symxns.sym.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

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
    @SymLog(sysModule = "微信接口",sysType = "微信接口",sysDesc = "微信接口")
    public Result api(@RequestBody HashMap<Object,Object> message){
        log.info("api message:{}", message);
//
//        //wxid_oa0rwmnimagm21
//
//        JSONObject param = JSONUtil.createObj();
//        param.set("id", message.id);
//        param.set("receiver", Arrays.asList("wxid_oa0rwmnimagm21"));
//
//        Map<String,String > headers = new HashMap();
//        headers.put("Authorization", "Bearer dcONwXuymAxEKVuJ1ietX1D01sXZ1pEw8B4T1rXXovx");
//        String body = HttpUtil.createPost("http://192.168.10.10:7600/wcf/forward_msg").body(param.toString()).addHeaders(headers).execute().body();
//
//        log.info(body);
        return Result.ok();
    }
}
