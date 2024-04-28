package com.yujigu.summer.iwebot.service;

import com.yujigu.summer.iwebot.gcw.GcwData;

import java.util.List;

public interface GcwService {
    //广场舞搜索
    List<GcwData> gcw(String name);

    //广场舞歌曲
    String gcwMp3(String name);
}
