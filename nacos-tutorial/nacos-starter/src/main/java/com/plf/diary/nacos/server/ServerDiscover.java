package com.plf.diary.nacos.server;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;

/**
 * @author panlf
 * @date 2023/4/24
 */
public class ServerDiscover {
    public static void main(String[] args) throws Exception{
        NamingService namingService = NamingFactory.createNamingService("localhost:8848");

        System.out.println(namingService.getAllInstances("user"));
    }
}
