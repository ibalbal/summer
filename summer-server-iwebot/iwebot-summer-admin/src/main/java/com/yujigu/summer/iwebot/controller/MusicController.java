package com.yujigu.summer.iwebot.controller;

import com.symxns.sym.core.result.Result;
import com.yujigu.summer.iwebot.MusicParams;
import com.yujigu.summer.iwebot.MusicRedirect;
import com.yujigu.summer.iwebot.MusicStatus;
import com.yujigu.summer.iwebot.service.MusicService;
import com.yujigu.summer.music.entity.MusicData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @GetMapping("/status")
    public Result<MusicStatus> status(){
        return Result.ok(musicService.musicService());
    }

    //请求重定向
    @PostMapping("/redirect")
    public Result<MusicRedirect> redirect(@RequestBody MusicParams params){
        return Result.ok(musicService.musicRedirect(params));
    }


    /**
     * 获取音乐详情
     * @param type 平台
     * @param hash  hash-目前酷狗歌曲hash
     * @return
     */
    @GetMapping("/details")
    public Result<MusicData> music(@RequestParam(value = "type", required = false) String type, @RequestParam("hash") String hash){
        return Result.ok(musicService.details(type, hash));
    }

}
