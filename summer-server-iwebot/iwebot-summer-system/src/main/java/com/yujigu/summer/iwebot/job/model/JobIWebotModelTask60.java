package com.yujigu.summer.iwebot.job.model;

import com.yujigu.summer.iwebot.model.JobIWebotModel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "job.task60")
public class JobIWebotModelTask60 extends JobIWebotModel {
}
