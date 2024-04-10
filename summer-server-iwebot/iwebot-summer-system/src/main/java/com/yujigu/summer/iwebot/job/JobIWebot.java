package com.yujigu.summer.iwebot.job;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yujigu.summer.iwebot.job.model.JobIWebotModel;
import com.yujigu.summer.iwebot.job.model.JobIWebotModelTask60;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JobIWebot {

    @Autowired
    public JobIWebotModelTask60 task60;

    @Scheduled(cron = "50 29 9 * * *")
    public void job60(){

        for (String receiver : task60.getReceivers()) {
            JSONObject param = JSONUtil.createObj();
            param.set("path", task60.getUrl());
            param.set("receiver", receiver);
            Map<String,String > headers = new HashMap();
            headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
            String resultbody = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_file").body(param.toString()).addHeaders(headers).execute().body();
            log.info(resultbody);
        }
    }
}
