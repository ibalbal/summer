package com.yujigu.summer.iwebot.service;

import com.yujigu.summer.iwebot.music.netease.MusicNeteaseModel;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface MusicNeteaseService {

    public void login(HttpServletResponse response);

    public String loginCheck();

    public String loginRefresh();

    List<MusicNeteaseModel> search(String keywords, int limit, int offset);

    MusicNeteaseModel songDetails(String id, String level);

    List<MusicNeteaseModel> playlisTop(int limit, int offset);

    List<MusicNeteaseModel> playlistHighquality(int limit, int before);

    List<MusicNeteaseModel> playlistHighqualitySong(String id, int limit, int offset);

    List<MusicNeteaseModel> simiSong(String id);

    List<MusicNeteaseModel> recommedSong();

    List<MusicNeteaseModel> newSong();
}
