package com.yujigu.summer.iwebot.music.netease;

import lombok.Data;

@Data
public class MusicNeteaseModel {
    private String id;
    private String title;
    private String singer;
    private String cover;
    private String url;
    private String lyric;
    private String updateTime;
}
