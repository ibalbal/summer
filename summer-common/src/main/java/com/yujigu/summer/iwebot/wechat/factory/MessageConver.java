package com.yujigu.summer.iwebot.wechat.factory;

import com.sun.tools.javac.Main;
import com.symxns.sym.core.result.ManageException;
import com.yujigu.summer.iwebot.wechat.WechatMessage;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
@Slf4j
public class MessageConver {
    public static Message init(WechatMessage wechatMessage){
        String type = wechatMessage.getType();

        // 类名（不包括包名）
        StringBuilder className = new StringBuilder("_");
        className.append(type);
        className.append("_MessageFactory");
        try {
            // 获取当前类的类加载器
            ClassLoader classLoader = MessageConver.class.getClassLoader();

            String path = MessageConver.class.getPackageName() + "." + className;
            // 通过类加载器和类名（不包括包名）获取 Class 对象
            Class<?> clazz = classLoader.loadClass(path);

            // 创建类的实例
            MessageFactory messageFactory = (MessageFactory) clazz.getDeclaredConstructor().newInstance();
            return messageFactory.createMessage();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            log.error("消息类型获取失败：{}", className);
        }

        throw new ManageException("未匹配的消息类型" + type);
    }

}
