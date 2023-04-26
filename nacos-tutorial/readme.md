## 启动
```
.\startup.cmd -m standalone
```

## 访问
```
访问地址：http://localhost:8848/nacos/#/login
用户名：nacos
密码：nacos
```

## 配置

```
# 优先去拿dataId=test.properties,而后才是test 
spring.application.name = test 
```

### 按profile拉取配置
在使用spring-cloud-starter-alibaba-nacos-config时，我们除开可以配置spring.cloud.nacos.config.server-addr外，还可以配置
- 1、spring.cloud.nacos.config.group		默认为：DEFAULT_GROUP
- 2、spring.cloud.nacos.config.file-extension 默认为：properties
- 3、spring.cloud.nacos.config.prefix		默认为：${spring.application.name}

```
spring.profiles.active=dev
拉取${spring.application.name}-dev.properties
```

### 拉取多个配置
一个应用可能不止需要一个配置，有时可能需要拉取多个配置，此时可以利用
- 1、spring.cloud.nacos.config.extension-configs[0].data_id=datasource.properties
- 2、spring.cloud.nacos.config.shared-configs[0].data_id=common.properties

- spring.cloud.nacos.config.shared-configs[0].refresh=true
- spring.cloud.nacos.config.shared-configs[0].file-extension=properties

extension-configs表示拉取额外的配置文件，shared-configs也表示拉取额外的配置文件，只不过
- 1、extension-configs表示本应用特有的
- 2、shared-configs表示多个应用共享的


优先级

`extension-configs[2] > extension-configs[1]  > extension-configs[0] `

`shared-configs[2] > shared-configs[1] > shared-configs[0]`

`主配置 > extension-configs > shared-configs`
