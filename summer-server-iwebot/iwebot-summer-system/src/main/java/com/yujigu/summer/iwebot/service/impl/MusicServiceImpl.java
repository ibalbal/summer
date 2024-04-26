package com.yujigu.summer.iwebot.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.symxns.sym.core.result.ManageException;
import com.symxns.sym.core.utils.BeanCopyUtils;
import com.yujigu.summer.iwebot.MusicParams;
import com.yujigu.summer.iwebot.MusicRedirect;
import com.yujigu.summer.iwebot.MusicStatus;
import com.yujigu.summer.iwebot.service.MusicService;
import com.yujigu.summer.music.entity.MusicData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class MusicServiceImpl implements MusicService {


    @Autowired
    private MusicStatus musicStatus;

    @Autowired
    private MusicRedirect musicRedirect;

    @Override
    public MusicStatus musicService() {
        return BeanCopyUtils.copyObject(musicStatus, MusicStatus.class);
    }

    @Override
    public MusicRedirect musicRedirect(MusicParams params) {
        MusicRedirect redirect = BeanCopyUtils.copyObject(musicRedirect, MusicRedirect.class);
        redirect.setUrl(redirect.getUrl().replaceAll("PARAMS", params.getContent()));
        return redirect;
    }

    @Override
    public MusicData details(String type, String hash) {
        String lyricUrl = "http://music.ibalbal.com/api/search/lyric?hash=HASH";
        String reqLyricUrl = lyricUrl.replace("HASH", hash);
        String body = HttpUtil.createGet(reqLyricUrl).execute().body();
        JSONObject parse = JSONObject.parse(body);
        Integer code = parse.getInteger("status");
        String msg = parse.getString("errmsg");
        if (code != 200) {
           throw  new ManageException(code , msg);
        }
        JSONObject candidates = parse.getJSONArray("candidates").getJSONObject(0);
        if (candidates == null) {
            throw new ManageException(500, "未查询到歌曲信息");
        }
        String id = candidates.getString("id");
        String accesskey = candidates.getString("accesskey");

        String lyricDetailsUrl = "http://music.ibalbal.com/api/lyric?id=ID&accesskey=ACCESSKEY&fmt=lrc&decode=true";
        String reqLyricDetailsUrl = lyricDetailsUrl.replace("ID", id).replace("ACCESSKEY", accesskey);
        String lyricBody = HttpUtil.createGet(reqLyricDetailsUrl).execute().body();
        JSONObject lyricJSON = JSONObject.parse(lyricBody);
        Integer lyricCode = lyricJSON.getInteger("status");
        String lyricMsg = lyricJSON.getString("errmsg");
        if (code != 200) {
            throw  new ManageException(lyricCode , lyricMsg);
        }
        //歌词
        String decodeContent = lyricJSON.getString("decodeContent");


        String musicUrl = "http://music.ibalbal.com/api/song/url?hash=HASH";
        String reqMusicUrl = musicUrl.replace("HASH", hash);
        String musicBbody = HttpUtil.createGet(reqMusicUrl).execute().body();
        JSONObject musicJSON = JSONObject.parse(musicBbody);
        //音乐
        String url = musicJSON.getJSONArray("url").getString(0);


        String details = "http://music.ibalbal.com/api/privilege/lite?hash=HASH";
        String detailsUrl = details.replace("HASH", hash);
        String detailsBody = HttpUtil.createGet(detailsUrl).execute().body();
        JSONObject detailsJSON = JSONObject.parse(detailsBody).getJSONArray("data").getJSONObject(0);
        String cover = detailsJSON.getJSONObject("info").getString("image").replaceAll("\\{size\\}","");
        String singername = detailsJSON.getString("singername");
        String albumname = detailsJSON.getString("albumname");

        MusicData musicData = new MusicData();
        musicData.setCover(cover);
        musicData.setLink(url);
        musicData.setLyrics(decodeContent);
        musicData.setSinger(singername);
        musicData.setTitle(albumname);
        musicData.setMusicUrl(url);
        return musicData;
    }


}
