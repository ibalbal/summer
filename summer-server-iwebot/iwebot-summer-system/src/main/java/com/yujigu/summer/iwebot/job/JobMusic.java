package com.yujigu.summer.iwebot.job;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobMusic {


    /**
     * 从0点开始 每三个小时执行一次
     */
    @Scheduled(cron = "0 0 */3 * * ?")
    public void kugouVip(){
        log.info("开始领取酷狗vip");
        String url = "http://music.ibalbal.com/api/youth/vip";
        String body = HttpUtil.createGet(url).execute().body();
        log.info("领取酷狗vip结束 -> body: {}", body);
    }

    /**
     * 每隔三天的凌晨4点执行一次任务。
     */
    @Scheduled(cron = "0 0 4 */3 * ?")
    public void updateKugouLogin(){
        log.info("开始刷新酷狗登录状态");
        String url = "http://music.ibalbal.com/api/login/token";
        String body = HttpUtil.createGet(url).execute().body();
        log.info("刷新酷狗登录状态结束 -> body: {}", body);
    }
}
