package com.yujigu.summer.iwebot.entity;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送 微信-消息
 */
@Slf4j
public abstract class Message implements Serializable {

    @Getter
    private String sendUrl;

    //接收人
    @Setter
    @Getter
    private String receiver;

    public boolean execute(String preUrl, String token) {
        String body = JSON.toJSON(this).toString();
        Map<String,String > headers = new HashMap();
        headers.put("Authorization", token);
        try{
            String result = HttpUtil
                    .createPost(preUrl + this.getSendUrl())
                    .addHeaders(headers)
                    .body(body)
                    .execute()
                    .body();
            log.info("发送消息请求响应:{}", result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            return jsonObject.getJSONObject("Payload").getBoolean("success");
        }catch (Exception e){
            log.error("发送异常:{}",e.getMessage());
            return false;
        }
    }

    public boolean execute() {
        String token = "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT";
        String preUrk = "http://192.168.10.10:7600";
        return execute(preUrk, token);
    }
}
