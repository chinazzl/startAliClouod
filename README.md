# 工程简介
Spring Cloud Alibaba 集成项目
集成 ~~nacos~~、~~mybatis plus~~、~~sentinel~~、~~redis~~ 、~~SpringSecurity~~
目前进行搭建一个简单框架，

| 模块名称              | 模块 描述            |
|-------------------|------------------|
| ali-cloud-common  | 公共模块，中间件基础配置，工具类 |
| ali-cloud-dao     | 持久层mybaitis-plus |
| ali-cloud-service | 业务服务层，对web层提供服务  |
| ali-cloud-web     | web层,服务入口        |


# 延伸阅读
### 项目结构
```txt
├── README.md
├── ali-cloud-api
│  └── src
│      └── main
│          └── java
│              └── com
│                  └── alicloud
│                      └── api
│                          ├── bean
│                          │  ├── dto
│                          │  │  └── UserLoginDto.java
│                          │  ├── package-info.java
│                          │  └── vo
│                          │      └── ModelVo.java
│                          └── service
│                              └── user
│                                  └── UserService.java
├── ali-cloud-common
│  └── src
│      └── main
│          └── java
│              └── com
│                  └── alicloud
│                      ├── annotation
│                      │  ├── LimitAccess.java
│                      │  └── UserLogin.java
│                      ├── aop
│                      │  └── LimitAccessAspect.java
│                      ├── config
│                      │  ├── CorsConfig.java
│                      │  ├── RedisConfig.java
│                      │  ├── UserLoginResolve.java
│                      │  ├── WebMvcConfig.java
│                      │  └── security
│                      │      ├── CustomSecurityExpression.java
│                      │      ├── LoginUser.java
│                      │      └── SecurityConfig.java
│                      ├── constant
│                      │  ├── JSONFeture.java
│                      │  └── RedisConstant.java
│                      ├── enums
│                      │  ├── BaseEnum.java
│                      │  ├── DelFlag.java
│                      │  ├── LimitType.java
│                      │  ├── ResultCode.java
│                      │  ├── Sex.java
│                      │  ├── UserStatus.java
│                      │  └── UserType.java
│                      ├── exception
│                      │  └── UserException.java
│                      ├── filter
│                      │  └── JwtAuthenticationTokenFilter.java
│                      ├── handler
│                      │  ├── AuthenticationEntryExceptionHandler.java
│                      │  └── DeniedAccessExceptionHandler.java
│                      ├── model
│                      │  ├── BaseModelVo.java
│                      │  ├── CommonResponse.java
│                      │  ├── Result.java
│                      │  └── UserVo.java
│                      └── utils
│                          ├── ByteUtils.java
│                          ├── CommonUtil.java
│                          ├── CookieUtils.java
│                          ├── IpUtils.java
│                          ├── LogIpConfig.java
│                          ├── RedisUtils.java
│                          ├── WebUtils.java
│                          └── jwt
│                              ├── JWTHelper.java
│                              ├── JWTInfo.java
│                              ├── JwtTokenUtil.java
│                              └── RsaKeyHelper.java
├── ali-cloud-dao
│  └── src
│      └── main
│          └── java
│              └── com
│                  └── alicloud
│                      ├── bean
│                      │  ├── Menu.java
│                      │  └── User.java
│                      ├── config
│                      │  ├── mybatis
│                      │  │  ├── AutoFillDataComponent.java
│                      │  │  ├── handler
│                      │  │  │  ├── CommonEnumTypeHandler.java
│                      │  │  │  ├── Fastjson2TypeHandler.java
│                      │  │  │  └── package-info.java
│                      │  │  ├── injector
│                      │  │  └── package-info.java
│                      │  └── package-info.java
│                      └── mapper
│                          └── UserMapper.java
├── ali-cloud-service
│  └── src
│      └── main
│          └── java
│              └── com
│                  └── alicloud
│                      └── service
│                          ├── config
│                          │  └── AliCloudServiceConfig.java
│                          └── impl
│                              ├── security
│                              │  └── UserDetailsServiceImpl.java
│                              └── user
│                                  └── UserServiceImpl.java
├── ali-cloud-web
│  ├── pom.xml
│  └── src
│      └── main
│          └── java
│              └── com
│                  └── alicloud
│                      └── web
│                          ├── AliCloudWeb.java
│                          └── controller
│                              └── UserController.java
```