package com.yujigu.summer.iwebot.service.impl;

import com.yujigu.summer.iwebot.service.GcwService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author ly
 * @Date 2024/4/22 22:57
 * @Description 广场舞
 */
@Slf4j
@Service
public class GcwServiceImpl implements GcwService {

    @Override
    public String gcw(String name) {
        String url = "https://www.gcwdp.com/plus/search.php?pagesize=20&q=NAME&channeltype=3";
        StringBuilder result = new StringBuilder();
        try {
            // 将字符串按照 GBK 编码转换成字节数组
            byte[] gbkBytes = name.getBytes("GBK");
            String coverName = bytesToHexString(gbkBytes);
            String requestUrl = url.replace("NAME", coverName);


            // 使用 Jsoup 发起 HTTP 请求并获取页面数据
            Document document = Jsoup.connect(requestUrl).get();

            // 获取 class 为 "ffso2" 的 div 元素
            Element ffso2Div = document.selectFirst("div.ffso2");

            // 获取所有的 li 元素
            Elements liElements = ffso2Div.select("li");

            // 遍历 li 元素，提取数据
            for (Element li : liElements) {
                // 获取 <a> 标签
                Element aTag = li.selectFirst("a");

                // 获取 href 属性
                String href = aTag.attr("href");

                // 获取 <span> 标签中的文本内容
                String text = li.selectFirst("span").text();

                result.append(text).append("\n");
                result.append("https://www.gcwdp.com" + href).append("\n");
                result.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    @Override
    public String gcwMp3(@RequestParam("url") String url) {
        StringBuilder result = new StringBuilder();
        // 提取 href 属性中的数值部分
        String[] parts = url.split("/");
        String value = parts[parts.length - 1];
        String id = parts[parts.length - 1].replace(".html", "");
        String Referer = "https://www.gcwdp.com/zxgcw/VALUE";
        String requestUrl = "https://www.gcwdp.com/plus/shitingx.php?id=ID";
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Referer", Referer.replace("VALUE", value));
            // 使用 Jsoup 发起 HTTP 请求并获取页面数据
            Document document = Jsoup.connect(requestUrl.replace("ID", id)).headers(headers).get();
            Element musicElement = document.selectFirst("script:containsData(music)");
            if (musicElement != null) {
                Pattern pattern = Pattern.compile("url: '([^']+)'");
                Matcher matcher = pattern.matcher(musicElement.data());
                if (matcher.find()) {
                    String musicUrl = matcher.group(1);
                    result.append("音乐 URL: ").append("\n");
                    result.append(musicUrl);
                    System.out.println("音乐 URL: " + musicUrl);
                } else {
                    System.out.println("未找到音乐 URL");
                }
            } else {
                System.out.println("未找到音乐 URL");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }



    // 将字节数组转换为十六进制字符串，并以 '%' 分隔
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (int i = 0; i < bytes.length; i++) {
            // 将字节转换为无符号整数
            int v = bytes[i] & 0xFF;
            // 转换为十六进制字符串
            String hv = Integer.toHexString(v);
            // 如果十六进制字符串长度为1，则在前面补0
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            // 添加十六进制字符串，并添加 '%' 分隔符
            stringBuilder.append('%').append(hv.toUpperCase());
        }
        return stringBuilder.toString();
    }

}
