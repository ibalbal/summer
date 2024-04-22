package com.yujigu.summer.iwebot.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/gcw")
public class GcwController {
    @RequestMapping("/search/{name}")
    public String gcw(@PathVariable("name") String name) {
       String url = "https://www.gcwdp.com/plus/search.php?pagesize=20&q=NAME&channeltype=3";


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

                // 输出 href 属性和文本内容
                System.out.println("href: https://www.gcwdp.com" + href);
                System.out.println("text: " + text);
                System.out.println();
            }

            System.out.println("字符串按照 GBK 编码转换成十六进制字符串：" + bytesToHexString(gbkBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "gcw";
    }

    @RequestMapping("/mp3")
    public void gcwMp3(@RequestParam("url") String url) {
        try {
            // 使用 Jsoup 发起 HTTP 请求并获取页面数据
            Document document = Jsoup.connect(url).get();

            // 获取 <iframe> 元素
            Element iframe = document.selectFirst("iframe");

            if (iframe != null) {
                // 获取 <iframe> 的 src 属性值
                String iframeSrc = iframe.attr("src");

                // 使用 Jsoup 发起 HTTP 请求并获取 <iframe> 中的页面数据
                Document iframeDocument = Jsoup.connect(iframeSrc).get();

                // 获取所有的 <script> 标签
                Elements scriptElements = iframeDocument.select("script");

                // 使用正则表达式提取 <script> 标签中的内容
                Pattern pattern = Pattern.compile("var\\s+ap3\\s+=\\s+new\\s+APlayer\\(\\{(.+?)\\}\\);", Pattern.DOTALL);

                for (Element script : scriptElements) {
                    // 获取 <script> 标签中的内容
                    String scriptContent = script.html();

                    // 在内容中匹配正则表达式
                    Matcher matcher = pattern.matcher(scriptContent);
                    while (matcher.find()) {
                        // 匹配成功，提取 url 值
                        String matchedContent = matcher.group(1);
                        Pattern urlPattern = Pattern.compile("url:\\s+'(.+?)'");
                        Matcher urlMatcher = urlPattern.matcher(matchedContent);
                        while (urlMatcher.find()) {
                            // 输出 url 值
                            String urlValue = urlMatcher.group(1);
                            System.out.println("url: " + urlValue);
                        }
                    }
                }
            } else {
                System.out.println("没有找到 <iframe> 元素");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
