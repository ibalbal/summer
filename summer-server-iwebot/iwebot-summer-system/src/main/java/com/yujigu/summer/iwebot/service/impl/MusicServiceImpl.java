package com.yujigu.summer.iwebot.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.symxns.sym.core.result.ManageException;
import com.yujigu.summer.iwebot.MusicStatus;
import com.yujigu.summer.iwebot.service.MusicService;
import com.yujigu.summer.music.entity.MusicData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class MusicServiceImpl implements MusicService {


    @Autowired
    private MusicStatus musicStatus;


    @Override
    public MusicStatus musicService() {
        return musicStatus;
    }

    @Override
    public MusicData details(String type, String hash) {
        String kgUrl = "https://www.hhlqilongzhu.cn/api/dg_kgmusic.php?hash=HASH&n=1&type=json";
        kgUrl = kgUrl.replace("HASH", hash);
        String body = HttpUtil.createPost(kgUrl).execute().body();
        JSONObject parse = JSONObject.parse(body);
        Integer code = parse.getInteger("code");
        String msg = parse.getString("msg");

        if (code != 200) {
           throw  new ManageException(code , msg);
        }

        MusicData musicData = JSONObject.parseObject(body, MusicData.class);
        return musicData;
    }
}
