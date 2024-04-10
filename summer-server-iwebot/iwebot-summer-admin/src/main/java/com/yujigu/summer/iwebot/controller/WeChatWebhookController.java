package com.yujigu.summer.iwebot.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.symxns.spring.log.annotation.SymLog;
import com.symxns.sym.core.result.Result;
import com.yujigu.summer.iwebot.yunxiao.Event;
import com.yujigu.summer.iwebot.yunxiao.GlobalParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/webhook/api")
public class WeChatWebhookController {

//    @Value(value = "${iwebot.sender}")
//    public String iwebotSender;

    @PostMapping("/{sendId}")
    @SymLog(sysModule = "微信接口",sysType = "微信接口",sysDesc = "微信接口", result = false)
    public void api(@PathVariable(name = "sendId") String sender,  @RequestBody Event message){
        log.info("event:{}",message);
        String commit = "";
        for (GlobalParam globalParam : message.getGlobalParams()) {
            if (globalParam.getKey().equals("CI_COMMIT_TITLE")){
                commit = globalParam.getValue();
                break;
            }
        }

        String msgFormat = "构建通知 %n%n" +
                "项目名称: %s%n" +
                "当前状态: %s%n";
        if (!ObjectUtils.isEmpty(commit)) {
            msgFormat += "提交信息: %s";
        }

        String msg = String.format(msgFormat,
                message.getTask().getPipelineName(),
                message.getTask().getStatusName(),
                (ObjectUtils.isEmpty(commit) ? "" : commit));

        JSONObject param = JSONUtil.createObj();
        param.set("msg", msg);
        param.set("receiver", sender);
        Map<String,String > headers = new HashMap();
        headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
        String body = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_txt").body(param.toString()).addHeaders(headers).execute().body();
        log.info(body);
    }
}
