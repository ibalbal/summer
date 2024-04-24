package com.yujigu.summer.iwebot.controller;

import com.symxns.sym.core.result.Result;
import com.yujigu.summer.iwebot.service.MusicService;
import com.yujigu.summer.music.entity.MusicData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    /**
     * 获取音乐详情
     * @param type 平台
     * @param hash  hash-目前酷狗歌曲hash
     * @return
     */
    @GetMapping("/details")
    public Result<MusicData> music(@RequestParam("type") String type, @RequestParam("hash") String hash){
        return Result.ok(musicService.details(type, hash));
    }

}
