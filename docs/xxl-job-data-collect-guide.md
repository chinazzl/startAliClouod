# XXL-Job定时采集任务使用指南

## 概述

本文档介绍基于XXL-Job的定时采集任务系统的使用方法，包括配置、部署和使用示例。

## 系统架构

```
XXL-Job Admin (调度中心)
    ↓
分片任务调度器
    ↓
多个执行器实例 (支持水平扩展)
    ↓
数据采集服务 → HTTP接口调用 → 数据存储
    ↓
数据过期处理 → 过期标记 → 数据清理
```

## 核心功能

### 1. HTTP数据采集
- 支持GET/POST请求
- 支持自定义请求头和参数
- 支持批量并发采集
- 支持重试机制

### 2. 分片执行
- 支持多实例水平扩展
- 自动负载均衡
- 数据一致性保证

### 3. 数据过期处理
- 自动标记过期数据
- 支持软删除和物理删除
- 可配置过期策略

## 配置说明

### 1. XXL-Job配置 (application-common.yml)

```yaml
xxl:
  job:
    admin:
      addresses: http://127.0.0.1:8080/xxl-job-admin  # XXL-Job调度中心地址
    accessToken: default_token                        # 访问令牌
    executor:
      appname: ali-cloud-data-collect                 # 执行器名称
      port: 9999                                      # 执行器端口
      logpath: /data/applogs/xxl-job/jobhandler       # 日志路径
      logretentiondays: 30                            # 日志保留天数
```

### 2. 数据库配置 (application.yml)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ali_cloud
    username: root
    password: root
```

### 3. 采集配置 (application.yml)

```yaml
data-collect:
  default-timeout: 30000      # 默认超时时间(毫秒)
  default-retry-count: 3      # 默认重试次数
  batch-size: 50              # 批量采集大小
  thread-pool-size: 10        # 线程池大小

data-expire:
  default-expire-days: 30     # 默认过期天数
  delete-batch-size: 1000     # 删除批次大小
  auto-delete-enabled: false  # 是否启用自动删除
```

## 任务配置示例

### 1. HTTP数据采集任务

在XXL-Job管理界面创建任务：

- **任务描述**: HTTP数据采集任务
- **负责人**: 开发人员
- **Cron表达式**: `0 0 2 * * ?` (每天凌晨2点执行)
- **运行模式**: 分片广播
- **任务参数**:

```json
{
  "apiUrl": "http://api.example.com/data/{dataId}",
  "method": "GET",
  "headers": {
    "Authorization": "Bearer token123",
    "Content-Type": "application/json"
  },
  "params": {
    "pageSize": 100
  },
  "timeout": 30000,
  "retryCount": 3,
  "batchSize": 50
}
```

### 2. 数据过期处理任务

- **任务描述**: 数据过期处理任务
- **负责人**: 开发人员
- **Cron表达式**: `0 0 3 * * ?` (每天凌晨3点执行)
- **运行模式**: 分片广播
- **任务参数**:

```json
{
  "expireThreshold": 30,
  "batchSize": 1000,
  "markExpiredEnabled": true,
  "markToDeleteEnabled": true,
  "deleteEnabled": false,
  "markToDeleteRatio": 0.1
}
```

## 数据库表结构

### 目标数据表示例 (target_table)

```sql
CREATE TABLE `target_table` (
  `id` varchar(64) NOT NULL COMMENT '主键ID',
  `data_content` text COMMENT '数据内容',
  `data_status` tinyint(4) DEFAULT '0' COMMENT '数据状态: 0-正常, 1-已过期, 2-待删除, 3-已删除',
  `collect_time` datetime DEFAULT NULL COMMENT '采集时间',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '逻辑删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_data_status` (`data_status`),
  KEY `idx_last_update_time` (`last_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采集数据表';
```

## 部署说明

### 1. XXL-Job调度中心部署

参考XXL-Job官方文档部署调度中心。

### 2. 执行器部署

1. 打包应用：
```bash
mvn clean package
```

2. 启动多个实例：
```bash
java -jar ali-cloud-service.jar --server.port=8082
java -jar ali-cloud-service.jar --server.port=8083
java -jar ali-cloud-service.jar --server.port=8084
```

3. 在XXL-Job管理界面注册执行器。

## 监控和日志

### 1. 任务执行日志

- XXL-Job管理界面查看任务执行日志
- 应用日志文件路径: `/data/applogs/xxl-job/jobhandler`

### 2. 关键指标监控

- 任务执行成功率
- 数据采集量统计
- 任务执行耗时
- 错误率和重试次数

## 常见问题

### 1. 分片不均匀

检查数据ID的哈希分布，确保数据均匀分布到各个分片。

### 2. 采集失败

- 检查目标接口是否正常
- 验证网络连接和防火墙设置
- 检查请求参数和认证信息

### 3. 性能问题

- 调整线程池大小和批次大小
- 优化数据库查询
- 增加执行器实例数量

## 扩展开发

### 1. 自定义采集逻辑

继承`BaseXxlJob`类实现自定义任务：

```java
@Component
public class CustomCollectJob extends BaseXxlJob {

    @Override
    protected String getTaskName() {
        return "自定义采集任务";
    }

    @Override
    protected void doExecute(String param) throws Exception {
        // 实现具体的采集逻辑
    }
}
```

### 2. 自定义数据处理

实现`DataCollectService`接口或扩展现有实现。

### 3. 增加新的数据状态

在`DataStatus`枚举中添加新的状态值。

## 注意事项

1. 确保数据库连接池配置合理
2. 监控内存和CPU使用情况
3. 定期清理日志文件
4. 配置合适的重试策略
5. 做好数据备份和恢复机制