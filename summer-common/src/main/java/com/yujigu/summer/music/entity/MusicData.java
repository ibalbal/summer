package com.yujigu.summer.music.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 音乐返回类
 */
@Data
public class MusicData implements Serializable {
    //标题
    private String title;
    //歌手
    private String singer;
    //封面
    private String cover;
    //web 链接
    private String link;
    //音乐
    @JsonProperty("music_url")
    private String musicUrl;
    //歌词
    private String lyrics;
}
