package com.plf.diary.naocs.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author panlf
 * @date 2023/4/3
 */
@RestController
public class NacosController {

    @NacosValue(value = "${nacos.name}",autoRefreshed = true)
    String nacosInfo;

    @GetMapping("getNacosInfo")
    public String getNacosInfo(){
        return nacosInfo;
    }

    @NacosInjected
    private NamingService namingService;

    @GetMapping(value = "/getInstance")
    public List<Instance> getInstance(@RequestParam String serviceName)
            throws NacosException {
        return namingService.getAllInstances(serviceName);
    }
}
