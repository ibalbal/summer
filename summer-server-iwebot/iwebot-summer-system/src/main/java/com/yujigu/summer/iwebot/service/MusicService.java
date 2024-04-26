package com.yujigu.summer.iwebot.service;

import com.yujigu.summer.iwebot.MusicParams;
import com.yujigu.summer.iwebot.MusicRedirect;
import com.yujigu.summer.iwebot.MusicStatus;
import com.yujigu.summer.music.entity.MusicData;

/**
 * 音乐服务类
 */
public interface MusicService {

    /**
     * 音乐服务状态
     * @return
     */
    MusicStatus musicService();


    /**
     * 音乐详情
     * @param type 平台类型-目前酷狗
     * @param hash 酷狗音乐hash
     * @return
     */
    MusicData details(String type, String hash);

    MusicRedirect musicRedirect(MusicParams params);
}
