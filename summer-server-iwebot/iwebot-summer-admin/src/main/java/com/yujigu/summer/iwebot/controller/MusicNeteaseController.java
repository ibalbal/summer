package com.yujigu.summer.iwebot.controller;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.symxns.spring.redis.service.RedisOperator;
import com.symxns.sym.core.result.ManageException;
import com.symxns.sym.core.result.Result;
import com.yujigu.summer.iwebot.music.netease.MusicNeteaseModel;
import com.yujigu.summer.iwebot.service.MusicNeteaseService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/music/netease")
public class MusicNeteaseController {

    @Autowired
    private MusicNeteaseService musicNeteaseService;

    @GetMapping("/login")
    public void login(HttpServletResponse response) {
        musicNeteaseService.login(response);
    }

    @GetMapping("/login/check")
    public String loginCheck() {
        return musicNeteaseService.loginCheck();
    }

    @GetMapping("/login/refresh")
    public String loginRefresh() {
        return musicNeteaseService.loginRefresh();
    }

    @GetMapping("/search")
    public Result<List<MusicNeteaseModel>> loginRefresh(@RequestParam("keywords") String keywords,
                                                        @RequestParam(value = "limit",defaultValue = "20") int limit,
                                                        @RequestParam(value = "offset", defaultValue = "0") int offset) {
        return Result.ok(musicNeteaseService.search(keywords, limit, offset));
    }

    /**
     * 音乐详情
     * @param id
     * @return
     */
    @GetMapping("/song/{id}")
    public Result<MusicNeteaseModel> song(@PathVariable("id") String id,
                                                  @RequestParam(value = "level", defaultValue = "jymaster") String level) {
        return Result.ok(musicNeteaseService.songDetails(id,level));
    }

    /**
     * 新歌
     * @return
     */
    @GetMapping("/new/song")
    public Result<List<MusicNeteaseModel>> newSong() {
        return Result.ok(musicNeteaseService.newSong());
    }

    /**
     * 相似歌曲
     * @param id
     * @return
     */
    @GetMapping("/simi/song/{id}")
    public Result<List<MusicNeteaseModel>> simiSong(@PathVariable("id") String id) {
        return Result.ok(musicNeteaseService.simiSong(id));
    }


    /**
     * 每日推荐
     */
    @GetMapping("/recommend/songs")
    public Result<List<MusicNeteaseModel>> recommendSong() {
        return Result.ok(musicNeteaseService.recommedSong());
    }

    /**
     * 精选歌单
     */
    @GetMapping("/playlist/top")
    public Result<List<MusicNeteaseModel>> playlistTop( @RequestParam(value = "limit", defaultValue = "20") int limit,
                                                        @RequestParam(value = "offset", defaultValue = "0") int offset) {
        return Result.ok(musicNeteaseService.playlisTop(limit, offset));
    }

    /**
     * 精选歌单
     */
    @GetMapping("/playlist/highquality")
    public Result<List<MusicNeteaseModel>> playlistHighquality( @RequestParam(value = "limit", defaultValue = "20") int limit,
                                                               @RequestParam(value = "before", defaultValue = "0") int before) {
        return Result.ok(musicNeteaseService.playlistHighquality(limit, before));
    }

    /**
     * 精选歌单详情
     */
    @GetMapping("/playlist/highquality/{id}")
    public Result<List<MusicNeteaseModel>> playlistHighqualitySong(@PathVariable("id") String id,
                                                                   @RequestParam(value = "limit", defaultValue = "20") int limit,
                                                                @RequestParam(value = "offset", defaultValue = "0") int offset) {
        return Result.ok(musicNeteaseService.playlistHighqualitySong(id, limit, offset));
    }

}
