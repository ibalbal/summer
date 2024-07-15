package com.yujigu.summer.iwebot.chain.factory;

import com.alibaba.fastjson2.JSONObject;
import com.symxns.sym.core.result.ManageException;
import com.yujigu.summer.iwebot.config.MessageConfig;
import com.yujigu.summer.iwebot.entity.ResultMessageForward;
import com.yujigu.summer.iwebot.wechat.EnumsMsgType;
import com.yujigu.summer.iwebot.wechat.body.WechatMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

@Slf4j
@Component
@Configuration
public class MessageConvert {
    @Autowired
    private MessageConfig messageConfig;

    public EnumsMsgType execute(String stringMessage) {
        WechatMessage wechatMessage = JSONObject.parseObject(stringMessage, WechatMessage.class);

        if (ObjectUtils.isEmpty(wechatMessage.getRoomid())){
            //消息转发
            Long messageId = wechatMessage.id;
            ResultMessageForward wxidOa0rwmnimagm21 = new ResultMessageForward(messageId, Collections.singletonList("wxid_oa0rwmnimagm21"));
            wxidOa0rwmnimagm21.execute(messageConfig.getUrl(), messageConfig.getToken());
        }


        String msgType = "_" + wechatMessage.getType();

        String type;
        try {
            type = EnumsMsgType.valueOf(msgType).getType();
        }catch (Exception e){
            throw new ManageException("未声明的消息类型" + wechatMessage.getType());
        }
        // 类名（不包括包名）
        StringBuilder className = new StringBuilder("_");
        className.append(type);
        className.append("_MessageFactory");
        try {
            // 获取当前类的类加载器
            ClassLoader classLoader = MessageConvert.class.getClassLoader();

            String path = MessageConvert.class.getPackageName() + "." + className;
            // 通过类加载器和类名（不包括包名）获取 Class 对象
            Class<?> clazz = classLoader.loadClass(path);

            // 创建类的实例
            MessageFactory messageFactory = (MessageFactory) clazz.getDeclaredConstructor().newInstance();

            //获取消息来源
            String sender = wechatMessage.Group ? wechatMessage.getRoomid() : wechatMessage.getSender();

            before(wechatMessage);
            EnumsMsgType execute = messageFactory.createMessage().execute(sender, stringMessage);
            post(wechatMessage);

            return execute;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            log.error("消息类型获取失败：{}", className);
        }

        throw new ManageException("未匹配的消息类型" + type);
    }

    //处理请求前 数据格式
    static WechatMessage before(WechatMessage wechatMessage) {
        log.info("前置处理通知");
        return wechatMessage;
    }

    //发送数据-请求
    static WechatMessage post(WechatMessage wechatMessage) {
        log.info("后置处理通知");
        return wechatMessage;
    }
}
