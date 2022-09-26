# 工程简介
Spring Cloud Alibaba 集成项目
集成 nacos、mybatis plus、sentinel、redis  
目前进士搭建一个简单框架，

| 模块名称              | 模块 描述            |
|-------------------|------------------|
| ali-cloud-common  | 公共模块，中间件基础配置，工具类 |
| ali-cloud-dao     | 持久层mybaitis-plus |
| ali-cloud-service | 业务层              |
| ali-cloud-web     | web层,服务入口        |


# 延伸阅读
### 项目结构
```txt
├─ali-cloud-common  
│  ├─src            
│  │  ├─main        
│  │  │  ├─java     
│  │  │  │  └─com   
│  │  │  │      └─alicloud
│  │  │  │          ├─config
│  │  │  │          └─utils
│  │  │  └─resources
│  │  └─test
│  │      └─java
│  │          └─com
│  │              └─alicloud
├─ali-cloud-dao
│  ├─src
│  │  ├─main
│  │  │  ├─java
│  │  │  │  └─com
│  │  │  │      └─alicloud
│  │  │  │          ├─entity
│  │  │  │          └─mapper
│  │  │  └─resources
│  │  │      └─mappings
│  │  └─test
│  │      └─java
│  │          └─com
│  │              └─alicloud
├─ali-cloud-service
│  ├─src
│  │  ├─main
│  │  │  └─java
│  │  │      └─com
│  │  │          └─alicloud
│  │  │              ├─config
│  │  │              ├─entity
│  │  │              └─service
│  │  │                  └─impl
│  │  └─test
│  │      └─java
│  │          └─com
│  │              └─alicloud
├─ali-cloud-web
│  ├─src
│  │  ├─main
│  │  │  ├─java
│  │  │  │  └─com
│  │  │  │      └─alicloud
│  │  │  │          └─web
│  │  │  │              ├─config
│  │  │  │              └─controller
│  │  │  └─resources
│  │  │      └─META-INF
│  │  └─test
│  │      └─java
│  │          └─ali
│  │              └─cloud
│  │                  └─test
│              └─cloud
│                  └─test
```