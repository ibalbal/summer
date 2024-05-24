package com.yujigu.summer.iwebot.controller;

import com.symxns.spring.log.annotation.SymLog;
import com.symxns.sym.core.result.Result;
import com.yujigu.summer.iwebot.service.GcwService;
import com.yujigu.summer.iwebot.wechat.EnumsMsgType;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import com.yujigu.summer.iwebot.chain.factory.MessageConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

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

    @Autowired
    private MessageConvert messageConvert;

    @PostMapping
    @SymLog(sysModule = "微信接口",sysType = "微信接口",sysDesc = "微信接口", result = false)
    public Result api(@RequestBody String message){
        log.info("---：{}", message);

        try{
//            WechatMessage wechatMessage = com.alibaba.fastjson2.JSONObject.parseObject(message, WechatMessage.class);
            messageConvert.execute(message);
            log.info("---：{}", message);
        }catch (Exception e){
            log.info("e:{}", e.getMessage());
        }
        return Result.ok();
    }

    public static void welcome(String message, String receiver){
//        MessageText messageText = new MessageText(message);
//        messageText.setReceiver(receiver);
//        messageText.execute();
    }

}
