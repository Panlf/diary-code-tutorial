package com.plf.diary.nacos.starter;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;

import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author panlf
 * @date 2023/4/3
 */
public class NacosStarter {
    public static void main(String[] args) {
        try {
            String serverAddr = "localhost:8848";
            String dataId = "test";
            String group = "DEFAULT_GROUP";

            Properties properties = new Properties();
            properties.put("serverAddr",serverAddr);

            ConfigService configService = NacosFactory.createConfigService(properties);

            String content = configService.getConfig(dataId,group,5000);

            System.out.println(content);

            //配置监听
            configService.addListener(dataId, group, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String s) {
                    System.out.println(s);
                }
            });

            System.in.read();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
