package com.yujigu.summer.iwebot;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableScheduling
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class iWebotApplication {

        public static void main(String[] args) {
            SpringApplication.run(iWebotApplication.class, args);
            JSONObject param = JSONUtil.createObj();
            param.set("msg", "机器人后台启动成功");
            param.set("receiver", "wxid_oa0rwmnimagm21");
            Map<String,String > headers = new HashMap();
            headers.put("Authorization", "Bearer KpnJuEdJVaNpjBjXOfBmTVuXQLNtzFSNwJNJffXEuydkRKTpdHbcjrCXYwotUYocMstxaNOsSstTzJrNjZVfAJqWRPQUeccpTT");
            String body = HttpUtil.createPost("http://192.168.10.10:7600/wcf/send_txt").body(param.toString()).addHeaders(headers).execute().body();
            log.info("机器人后台启动成功:{}",body);
        }

}