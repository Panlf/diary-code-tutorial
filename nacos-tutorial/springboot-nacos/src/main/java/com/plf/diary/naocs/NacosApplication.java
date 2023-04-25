package com.plf.diary.naocs;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author panlf
 * @date 2023/4/3
 */
@SpringBootApplication
//可在application.properties中配置
//@NacosPropertySource(dataId = "test",autoRefreshed = true)
public class NacosApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosApplication.class,args);
    }
}
