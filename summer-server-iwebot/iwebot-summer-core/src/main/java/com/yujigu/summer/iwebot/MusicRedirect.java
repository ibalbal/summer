package com.yujigu.summer.iwebot;

import com.yujigu.summer.music.entity.MusicData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "music.redirect")
@RefreshScope
public class MusicRedirect implements Serializable {
    private String url;

    private MusicData data;
}
