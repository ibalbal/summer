package com.yujigu.summer.iwebot.service.impl;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.UserAgent;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.symxns.spring.redis.service.RedisOperator;
import com.symxns.sym.core.result.ManageException;
import com.symxns.sym.core.utils.UserAgentUtil;
import com.yujigu.summer.iwebot.music.netease.MusicNeteaseModel;
import com.yujigu.summer.iwebot.service.MusicNeteaseService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MusicNeteaseServiceImpl implements MusicNeteaseService {

    @Autowired
    private RedisOperator<String> redisOperator;

    private static final String URL_SERVER = "http://47.116.215.81:3000";

    /**
     * 1. 二维码 key 生成接口
     * 说明: 调用此接口可生成一个 key
     */
    private static final String URL_QR_KEY = URL_SERVER + "/login/qr/key";

    /**
     * 2. 二维码生成接口
     * 说明: 调用此接口传入上一个接口生成的 key 可生成二维码图片的 base64 和二维码信息,可使用 base64 展示图片,或者使用二维码信息内容自行使用第三方二维码生成库渲染二维码
     *
     * 必选参数: key,由第一个接口生成
     *
     * 可选参数: qrimg 传入后会额外返回二维码图片 base64 编码
     */
    private static final String URL_QR_CREATE = URL_SERVER + "/login/qr/create";

    /**
     * 3. 二维码检测扫码状态接口
     * 说明: 轮询此接口可获取二维码扫码状态,800 为二维码过期,801 为等待扫码,802 为待确认,803 为授权登录成功(803 状态码下会返回 cookies),如扫码后返回502,则需加上noCookie参数,如&noCookie=true
     *
     * 必选参数: key,由第一个接口生成
     */
    private static final String URL_QR_CHECK = URL_SERVER + "/login/qr/check";

    /**
     * 刷新登录
     * 说明 : 调用此接口 , 可刷新登录状态,返回内容包含新的cookie(不支持刷新二维码登录的cookie)
     */
    private static final String URL_QR_REFRESH = URL_SERVER + "/login/refresh";


    private static final String URL_SEARCH = URL_SERVER + "/cloudsearch";

    private static final String URL_SONG_URL = URL_SERVER + "/song/url/v1";
    private static final String URL_SONG_DETAIL_LYRIC = URL_SERVER + "/lyric";
    private static final String URL_SONG_DETAIL = URL_SERVER + "/song/detail";
    private static final String URL_PLAYLIST_HIGHQUALITY = URL_SERVER + "/top/playlist/highquality";
    private static final String URL_PLAYLIST_TOP = URL_SERVER + "/top/playlist";
    private static final String URL_PLAYLIST_HIGHQUALITY_SONG = URL_SERVER + "/playlist/track/all";
    private static final String URL_SIMI_SONG = URL_SERVER + "/simi/song";
    private static final String URL_RECOMMEND_SONGS = URL_SERVER + "/recommend/songs";
    private static final String URL_NEW_SONG = URL_SERVER + "/personalized/newsong";
    @Override
    public void login(HttpServletResponse response) {
        String url = URL_QR_KEY + "?t=" + System.currentTimeMillis();

        String body = HttpUtil.createGet(url).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONObject data = jsonObject.getJSONObject("data");
        if ("200".equals(jsonObject.getString("code"))){
            String unikey = data.getString("unikey");

            String qrUrl = URL_QR_CREATE + "?key=" + unikey +  "&t=" + System.currentTimeMillis();

            String qrBody = HttpUtil.createGet(qrUrl).execute().body();
            JSONObject jsonObjectQR = JSONObject.parseObject(qrBody);
            JSONObject dataQr = jsonObjectQR.getJSONObject("data");
            if ("200".equals(jsonObjectQR.getString("code"))){
                String qrimg = dataQr.getString("qrurl");
                response.setContentType("image/png");
                response.setHeader("unikey", unikey);
                redisOperator.set("MUSIC:UNIKEY:", unikey, 5, TimeUnit.MINUTES);
                try {
                    QrCodeUtil.generate(qrimg, 300, 300, "png", response.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                throw new ManageException("获取二维码失败");
            }
        }else {
            throw new ManageException("获取key失败");
        }
    }

    @Override
    public String loginCheck() {
        String key = redisOperator.get("MUSIC:UNIKEY:");
        String url = URL_QR_CHECK + "?key=" + key + "&t=" + System.currentTimeMillis();
        String body = HttpUtil.createGet(url).execute().body();
        return body;
    }

    @Override
    public String loginRefresh() {
        String url = URL_QR_REFRESH + "?t=" + System.currentTimeMillis();
        String body = HttpUtil.createGet(url).execute().body();
        return body;
    }

    @Override
    public List<MusicNeteaseModel> search(String keywords, int limit, int offset) {
        String url = URL_SEARCH + "?keywords=" + keywords + "&limit=" + limit + "&offset=" + offset + "&t=" + System.currentTimeMillis();

        String jsonObject = HttpUtil.createGet(url).execute().body();
        JSONObject body = JSONObject.parseObject(jsonObject);
        if ("200".equals(body.getString("code"))){
            JSONObject result = body.getJSONObject("result");
            JSONArray songs = result.getJSONArray("songs");

            List<MusicNeteaseModel> musicNeteaseModels = new ArrayList<>();
            for (int i = 0; i < songs.size(); i++) {
                JSONObject song = songs.getJSONObject(i);
                String id = song.getString("id");
                String name = song.getString("name");
                String picUrl = song.getJSONObject("al").getString("picUrl");
                MusicNeteaseModel musicNeteaseModel = new MusicNeteaseModel();
                musicNeteaseModel.setId(id);
                musicNeteaseModel.setTitle(name);
                musicNeteaseModel.setCover(picUrl);
                String singer = song.getJSONArray("ar").getJSONObject(0).getString("name");
                musicNeteaseModel.setSinger(singer);
                musicNeteaseModel.setSinger(singer);

                musicNeteaseModels.add(musicNeteaseModel);
            }
            return musicNeteaseModels;
        }
        return null;
    }

    @Override
    public MusicNeteaseModel songDetails(String id, String level) {
        MusicNeteaseModel neteaseModel = new MusicNeteaseModel();
        neteaseModel.setId(id);
        this.picName(neteaseModel, id);
        this.payUrl(neteaseModel, id, level);
        this.lyric(neteaseModel, id);
        return neteaseModel;
    }

    @Override
    public List<MusicNeteaseModel> playlistHighquality(int limit, int before) {
        String url = URL_PLAYLIST_HIGHQUALITY + "?limit=" + limit + "&before=" + before ;
        String body = HttpUtil.createGet(url).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        List<MusicNeteaseModel> playlist = new ArrayList<>();
        if ("200".equals(jsonObject.getString("code"))){
            JSONArray playlists = jsonObject.getJSONArray("playlists");
            for (int i = 0; i < playlists.size(); i++) {
                JSONObject data = playlists.getJSONObject(i);
                String id = data.getString("id");
                String name = data.getString("name");
                String cover = data.getString("coverImgUrl");
                String updateTime = data.getString("updateTime");
                MusicNeteaseModel musicNeteaseModel = new MusicNeteaseModel();
                musicNeteaseModel.setId(id);
                musicNeteaseModel.setTitle(name);
                musicNeteaseModel.setCover(cover);
                musicNeteaseModel.setUpdateTime(updateTime);
                playlist.add(musicNeteaseModel);
            }
            return playlist;
        }
        return null;
    }

    @Override
    public List<MusicNeteaseModel> playlistHighqualitySong(String pid, int limit, int offset) {
        String url = URL_PLAYLIST_HIGHQUALITY_SONG + "?id=" + pid + "&limit=" + limit + "&offset=" + offset ;
        String body = HttpUtil.createGet(url).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        if ("200".equals(jsonObject.getString("code"))){
            JSONArray songs= jsonObject.getJSONArray("songs");
            List<MusicNeteaseModel> musicNeteaseModels = new ArrayList<>();
            for (int i = 0; i < songs.size(); i++) {
                JSONObject song = songs.getJSONObject(i);
                String id = song.getString("id");
                String name = song.getString("name");
                String picUrl = song.getJSONObject("al").getString("picUrl");
                MusicNeteaseModel musicNeteaseModel = new MusicNeteaseModel();
                musicNeteaseModel.setId(id);
                musicNeteaseModel.setTitle(name);
                musicNeteaseModel.setCover(picUrl);
                String singer = song.getJSONArray("ar").getJSONObject(0).getString("name");
                musicNeteaseModel.setSinger(singer);
                musicNeteaseModel.setSinger(singer);
                musicNeteaseModels.add(musicNeteaseModel);
            }
            return musicNeteaseModels;
        }
        return null;
    }

    @Override
    public List<MusicNeteaseModel> simiSong(String id) {
        String url = URL_SIMI_SONG + "?id=" + id ;
        String body = HttpUtil.createGet(url).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        if ("200".equals(jsonObject.getString("code"))){
            JSONArray songs = jsonObject.getJSONArray("songs");
            List<MusicNeteaseModel> musicNeteaseModels = new ArrayList<>();
            for (int i = 0; i < songs.size(); i++) {
                MusicNeteaseModel musicNeteaseModel = new MusicNeteaseModel();

                JSONObject song = songs.getJSONObject(i);
                JSONObject album = song.getJSONObject("album");
                String picUrl = album.getString("picUrl");
                String name = album.getString("name");
                String ids = song.getString("id");

                JSONObject artists = song.getJSONArray("artists").getJSONObject(0);
                String singer = artists.getString("name");
                musicNeteaseModel.setSinger(singer);
                musicNeteaseModel.setId(ids);
                musicNeteaseModel.setTitle(name);
                musicNeteaseModel.setCover(picUrl);
                musicNeteaseModels.add(musicNeteaseModel);
            }
            return musicNeteaseModels;
        }
        return null;
    }

    @Override
    public List<MusicNeteaseModel> recommedSong() {
        String url = URL_RECOMMEND_SONGS;
        String body = HttpUtil.createGet(url).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        if ("200".equals(jsonObject.getString("code"))){
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray songs = data.getJSONArray("dailySongs");

            List<MusicNeteaseModel> musicNeteaseModels = new ArrayList<>();
            for (int i = 0; i < songs.size(); i++) {
                MusicNeteaseModel neteaseModel = new MusicNeteaseModel();
                JSONObject song = songs.getJSONObject(i);
                JSONObject al = song.getJSONObject("al");

                String singer = song.getJSONArray("ar").getJSONObject(0).getString("name");
                neteaseModel.setSinger(singer);
                String name = al.getString("name");
                String picUrl = al.getString("picUrl");
                neteaseModel.setTitle(name);
                neteaseModel.setCover(picUrl);
                musicNeteaseModels.add(neteaseModel);
            }
            return musicNeteaseModels;
        }
        return null;
    }

    @Override
    public List<MusicNeteaseModel> newSong() {
        String url = URL_NEW_SONG;
        String body = HttpUtil.createGet(url).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        if ("200".equals(jsonObject.getString("code"))){
            JSONArray songs = jsonObject.getJSONArray("result");
            List<MusicNeteaseModel> musicNeteaseModels = new ArrayList<>();
            for (int i = 0; i < songs.size(); i++) {
                MusicNeteaseModel musicNeteaseModel = new MusicNeteaseModel();

                JSONObject song = songs.getJSONObject(i);
                String picUrl = song.getString("picUrl");
                String name = song.getString("name");
                String ids = song.getString("id");

                JSONObject artists = song.getJSONObject("song").getJSONArray("artists").getJSONObject(0);
                String singer = artists.getString("name");
                musicNeteaseModel.setSinger(singer);
                musicNeteaseModel.setId(ids);
                musicNeteaseModel.setTitle(name);
                musicNeteaseModel.setCover(picUrl);
                musicNeteaseModels.add(musicNeteaseModel);
            }
            return musicNeteaseModels;
        }
        return null;
    }

    @Override
    public List<MusicNeteaseModel> playlisTop(int limit, int offset) {
        String url = URL_PLAYLIST_TOP + "?limit=" + limit + "&offset=" + offset ;
        String body = HttpUtil.createGet(url).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        List<MusicNeteaseModel> playlist = new ArrayList<>();
        if ("200".equals(jsonObject.getString("code"))){
            JSONArray playlists = jsonObject.getJSONArray("playlists");
            for (int i = 0; i < playlists.size(); i++) {
                JSONObject data = playlists.getJSONObject(i);
                String id = data.getString("id");
                String name = data.getString("name");
                String cover = data.getString("coverImgUrl");
                String updateTime = data.getString("updateTime");
                MusicNeteaseModel musicNeteaseModel = new MusicNeteaseModel();
                musicNeteaseModel.setId(id);
                musicNeteaseModel.setTitle(name);
                musicNeteaseModel.setCover(cover);
                musicNeteaseModel.setUpdateTime(updateTime);
                playlist.add(musicNeteaseModel);
            }
            return playlist;
        }
        return null;
    }


    void payUrl(MusicNeteaseModel neteaseModel, String id, String level){
        String url = URL_SONG_URL + "?id=" + id + "&level="+ level;
        String body = HttpUtil.createGet(url).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        if ("200".equals(jsonObject.getString("code"))){
            JSONArray data = jsonObject.getJSONArray("data");
            JSONObject song = data.getJSONObject(0);
            String payUrl = song.getString("url");
            neteaseModel.setUrl(payUrl);
        }
    }

    void lyric(MusicNeteaseModel neteaseModel, String id){
        String url = URL_SONG_DETAIL_LYRIC + "?id=" + id ;
        String body = HttpUtil.createGet(url).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        if ("200".equals(jsonObject.getString("code"))){
            JSONObject lrc = jsonObject.getJSONObject("lrc");
            String lyric = lrc.getString("lyric");
            neteaseModel.setLyric(lyric);
        }
    }

    void picName(MusicNeteaseModel neteaseModel, String id){
        String url = URL_SONG_DETAIL + "?ids=" + id ;
        String body = HttpUtil.createGet(url).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        if ("200".equals(jsonObject.getString("code"))){
            JSONArray data = jsonObject.getJSONArray("songs");
            JSONObject song = data.getJSONObject(0);
            JSONObject al = song.getJSONObject("al");

            String singer = song.getJSONArray("ar").getJSONObject(0).getString("name");
            neteaseModel.setSinger(singer);


            String name = al.getString("name");
            String picUrl = al.getString("picUrl");
            neteaseModel.setTitle(name);
            neteaseModel.setCover(picUrl);
        }
    }
}
