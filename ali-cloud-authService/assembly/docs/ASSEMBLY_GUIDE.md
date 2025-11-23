# Assembly 打包部署指南

## 📋 概述

本指南介绍如何使用 Maven Assembly 插件为 AliCloud Web 项目创建完整的应用部署包。

## 🎯 功能特性

- ✅ 多环境支持 (dev/test/prod)
- ✅ 依赖包管理
- ✅ 配置文件隔离
- ✅ 启动脚本 (Linux/Windows)
- ✅ 健康检查脚本
- ✅ 完整文档
- ✅ 版本信息管理

## 🚀 使用方法

### 1. 环境准备

确保已安装：
- Maven 3.6+
- Java 8+
- Git

### 2. 打包命令

```bash
# 开发环境打包
mvn clean package -P dev

# 生产环境打包
mvn clean package -P prod

# 测试环境打包
mvn clean package -P test

# 跳过测试打包
mvn clean package -P prod -DskipTests
```

### 3. 打包产物

打包完成后，在 `target/` 目录下会生成：
- `ali-cloud-web.jar` - Spring Boot JAR 包
- `ali-cloud-web-0.0.1-SNAPSHOT-prod.tar.gz` - 完整部署包 (生产环境)
- `ali-cloud-web-0.0.1-SNAPSHOT-prod.zip` - ZIP格式部署包

### 4. 部署包结构

解压部署包后的目录结构：
```
ali-cloud-web-0.0.1-SNAPSHOT-prod/
├── bin/                    # 启动脚本
├── config/                 # 配置文件
├── lib/                    # JAR包和依赖
├── logs/                   # 日志目录 (运行时创建)
├── scripts/                # 工具脚本
├── docs/                   # 文档
├── temp/                   # 临时文件目录
├── README.md               # 环境说明文档
└── VERSION                 # 版本信息文件
```

## 🔧 配置说明

### Assembly 配置文件

主要配置文件：`assembly/assembly.xml`

- **依赖管理**: 自动收集所有runtime依赖
- **配置文件**: 根据环境选择对应配置
- **文件权限**: 设置脚本文件执行权限
- **打包格式**: 支持tar.gz和zip格式

### Maven Profile 配置

在根目录 `pom.xml` 中配置：
```xml
<profiles>
    <profile>
        <id>dev</id>
        <properties>
            <profiles.active>dev</profiles.active>
        </properties>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
    </profile>
    <profile>
        <id>prod</id>
        <properties>
            <profiles.active>prod</profiles.active>
        </properties>
    </profile>
</profiles>
```

## 📝 自定义配置

### 1. 修改JVM参数

编辑 `assembly/bin/start.sh`：
```bash
# 调整JVM参数
JVM_OPTS="-Xms1024m -Xmx4096m"
```

### 2. 添加环境配置

1. 创建环境目录：`src/main/resources/custom/`
2. 添加配置文件：`src/main/resources/custom/application.yml`
3. 添加Maven profile配置
4. 更新assembly配置

### 3. 自定义脚本

在 `assembly/scripts/` 目录下添加自定义脚本：
- 数据备份脚本
- 监控脚本
- 数据库迁移脚本

## 🔄 CI/CD 集成

### Jenkins Pipeline 示例

```groovy
pipeline {
    agent any

    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'test', 'prod'], description: '选择部署环境')
    }

    stages {
        stage('Build') {
            steps {
                sh "mvn clean package -P ${params.ENVIRONMENT}"
            }
        }

        stage('Deploy') {
            steps {
                sh """
                scp target/ali-cloud-web-*.tar.gz deploy@server:/opt/apps/
                ssh deploy@server 'cd /opt/apps && tar -xzf ali-cloud-web-*.tar.gz && cd ali-cloud-web-* && ./bin/start.sh ${params.ENVIRONMENT} restart'
                """
            }
        }
    }
}
```

### GitLab CI 示例

```yaml
deploy:
  stage: deploy
  script:
    - mvn clean package -P $CI_ENVIRONMENT_NAME
    - scp target/ali-cloud-web-*.tar.gz $DEPLOY_USER@$DEPLOY_HOST:/opt/apps/
    - ssh $DEPLOY_USER@$DEPLOY_HOST "cd /opt/apps && tar -xzf ali-cloud-web-*.tar.gz && cd ali-cloud-web-* && ./bin/start.sh $CI_ENVIRONMENT_NAME restart"
  only:
    - master
    - develop
  when: manual
```

## 📊 最佳实践

### 1. 版本管理
- 使用语义化版本号
- 在CI/CD中自动构建版本号
- 保留历史版本的部署包

### 2. 环境隔离
- 每个环境独立的配置文件
- 使用环境变量管理敏感信息
- 配置文件加密存储

### 3. 安全考虑
- 启动脚本设置合适的文件权限
- 敏感配置文件加密
- 生产环境禁用调试功能

### 4. 监控和日志
- 集成健康检查
- 配置日志收集
- 设置关键指标监控

## 🐛 故障排除

### 打包失败
```bash
# 清理Maven缓存
mvn clean

# 检查依赖冲突
mvn dependency:tree

# 强制更新依赖
mvn clean package -U -P prod
```

### 启动失败
```bash
# 检查Java版本
java -version

# 查看详细错误信息
./bin/start.sh prod start
tail -f logs/startup.log

# 检查配置文件
cat config/application.yml.bak.bak
```

### 性能问题
```bash
# 生成JVM堆转储
jmap -dump:format=b,file=heapdump.hprof <pid>

# 分析GC情况
jstat -gc <pid> 5s
```

## 📞 技术支持

如有问题，请联系：
- **开发团队**: dev-team@company.com
- **运维团队**: ops-team@company.com
- **项目负责人**: pm@company.com

## 📚 相关文档

- [Spring Boot 部署指南](https://spring.io/guides/topicals/spring-boot-production)
- [Maven Assembly 插件文档](https://maven.apache.org/plugins/maven-assembly-plugin/)
- [项目内部Wiki](http://wiki.company.com/ali-cloud-web)