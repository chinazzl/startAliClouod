package com.alicloud.common.job;

import com.alicloud.common.enums.JobStatus;
import com.alicloud.common.model.CollectTaskLog;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;

/**
 * XXL-Job任务基类
 * 提供通用的任务执行框架和日志记录功能
 */
@Slf4j
public abstract class BaseXxlJob {

    /**
     * 执行采集任务
     * @param param 任务参数
     * @throws Exception 执行异常
     */
    protected abstract void doExecute(String param) throws Exception;

    /**
     * 获取任务名称
     * @return 任务名称
     */
    protected abstract String getTaskName();

    /**
     * 获取XXL-Job任务标识
     * 子类必须实现此方法来定义唯一的任务名称
     * @return XXL-Job任务名称
     */
    protected abstract String getJobName();

    /**
     * XXL-Job任务执行入口
     * 子类需要使用@XxlJob注解并调用此方法
     * @param param 任务参数
     */
    protected void execute(String param) {
        CollectTaskLog taskLog = createTaskLog(param);
        StopWatch stopWatch = new StopWatch();

        try {
            log.info("开始执行任务: {}, 参数: {}, 分片信息: {}/{}",
                getTaskName(), param,
                XxlJobHelper.getShardIndex(), XxlJobHelper.getShardTotal());

            taskLog.setStartTime(LocalDateTime.now());
            taskLog.setStatus(JobStatus.RUNNING);

            stopWatch.start();

            // 执行具体任务逻辑
            doExecute(param);

            stopWatch.stop();
            taskLog.setEndTime(LocalDateTime.now());
            taskLog.setStatus(JobStatus.SUCCESS);
            taskLog.setResultMsg(String.format("任务执行成功，耗时: %d ms", stopWatch.getTotalTimeMillis()));

            log.info("任务执行成功: {}, 耗时: {} ms", getTaskName(), stopWatch.getTotalTimeMillis());

        } catch (Exception e) {
            log.error("任务执行失败: {}, 异常信息: {}", getTaskName(), e.getMessage(), e);

            taskLog.setEndTime(LocalDateTime.now());
            taskLog.setStatus(JobStatus.FAILED);
            taskLog.setResultMsg("任务执行失败: " + e.getMessage());
            taskLog.setExceptionStack(getStackTrace(e));

            XxlJobHelper.handleFail(e.getMessage());

        } finally {
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }

            // 记录任务执行日志
            recordTaskLog(taskLog);

            // 任务执行完成回调
            afterExecute(taskLog);
        }
    }

    /**
     * 创建任务日志对象
     * @param param 任务参数
     * @return 任务日志对象
     */
    private CollectTaskLog createTaskLog(String param) {
        CollectTaskLog taskLog = new CollectTaskLog();
        taskLog.setTaskId(String.valueOf(XxlJobHelper.getJobId()));
        taskLog.setTaskName(getTaskName());
        taskLog.setTaskParam(param);
        taskLog.setShardTotal(XxlJobHelper.getShardTotal());
        taskLog.setShardIndex(XxlJobHelper.getShardIndex());
        taskLog.setCreateTime(LocalDateTime.now());
        return taskLog;
    }

    /**
     * 记录任务执行日志（子类可重写实现自定义日志记录）
     * @param taskLog 任务日志
     */
    protected void recordTaskLog(CollectTaskLog taskLog) {
        // 默认实现：打印日志
        log.info("任务日志: {}", taskLog);
    }

    /**
     * 任务执行完成后的回调方法（子类可重写实现自定义逻辑）
     * @param taskLog 任务日志
     */
    protected void afterExecute(CollectTaskLog taskLog) {
        // 默认空实现
    }

    /**
     * 获取异常堆栈信息
     * @param e 异常对象
     * @return 异常堆栈字符串
     */
    private String getStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.toString()).append("\n");

        for (StackTraceElement element : e.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }

        return sb.toString();
    }

    /**
     * 记录采集统计信息
     * @param total 总数
     * @param success 成功数
     * @param failed 失败数
     */
    protected void recordCollectStats(int total, int success, int failed) {
        log.info("采集统计 - 总数: {}, 成功: {}, 失败: {}", total, success, failed);
    }

    /**
     * 分片任务判断是否为当前分片处理
     * @param dataId 数据ID
     * @return 是否属于当前分片
     */
    protected boolean belongsToCurrentShard(String dataId) {
        if (XxlJobHelper.getShardTotal() <= 1) {
            return true;
        }

        // 简单的哈希分片策略
        int shardIndex = Math.abs(dataId.hashCode()) % XxlJobHelper.getShardTotal();
        return shardIndex == XxlJobHelper.getShardIndex();
    }
}