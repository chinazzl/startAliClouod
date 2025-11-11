package com.alicloud.common.model;

import com.alicloud.common.enums.JobStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 采集任务日志模型
 */
@Data
@Accessors(chain = true)
public class CollectTaskLog {

    /**
     * 日志ID
     */
    private Long id;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 分片总数
     */
    private Integer shardTotal;

    /**
     * 当前分片序号
     */
    private Integer shardIndex;

    /**
     * 任务参数
     */
    private String taskParam;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 任务状态
     */
    private JobStatus status;

    /**
     * 采集数量
     */
    private Integer collectCount;

    /**
     * 成功数量
     */
    private Integer successCount;

    /**
     * 失败数量
     */
    private Integer failedCount;

    /**
     * 执行结果信息
     */
    private String resultMsg;

    /**
     * 异常堆栈
     */
    private String exceptionStack;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}