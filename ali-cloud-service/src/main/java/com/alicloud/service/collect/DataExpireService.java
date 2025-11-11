package com.alicloud.service.collect;

import com.alicloud.common.enums.DataStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据过期处理服务
 */
public interface DataExpireService {

    /**
     * 标记过期数据
     * @param expireThreshold 过期阈值（天数）
     * @return 处理的记录数
     */
    int markExpiredData(int expireThreshold);

    /**
     * 标记指定状态的过期数据
     * @param currentStatus 当前状态
     * @param expireThreshold 过期阈值（天数）
     * @return 处理的记录数
     */
    int markExpiredDataByStatus(DataStatus currentStatus, int expireThreshold);

    /**
     * 删除已标记为过期的数据
     * @param batchSize 批次大小
     * @return 删除的记录数
     */
    int deleteExpiredData(int batchSize);

    /**
     * 获取过期数据统计
     * @return 过期数据统计信息
     */
    ExpiredDataStatistics getExpiredDataStatistics();

    /**
     * 批量更新数据状态
     * @param dataIds 数据ID列表
     * @param fromStatus 原状态
     * @param toStatus 目标状态
     * @return 更新的记录数
     */
    int updateDataStatus(List<String> dataIds, DataStatus fromStatus, DataStatus toStatus);

    /**
     * 过期数据统计信息
     */
    class ExpiredDataStatistics {
        /**
         * 正常数据数量
         */
        private long normalCount;

        /**
         * 已过期数据数量
         */
        private long expiredCount;

        /**
         * 待删除数据数量
         */
        private long toDeleteCount;

        /**
         * 统计时间
         */
        private LocalDateTime statisticsTime;

        // getters and setters
        public long getNormalCount() { return normalCount; }
        public void setNormalCount(long normalCount) { this.normalCount = normalCount; }
        public long getExpiredCount() { return expiredCount; }
        public void setExpiredCount(long expiredCount) { this.expiredCount = expiredCount; }
        public long getToDeleteCount() { return toDeleteCount; }
        public void setToDeleteCount(long toDeleteCount) { this.toDeleteCount = toDeleteCount; }
        public LocalDateTime getStatisticsTime() { return statisticsTime; }
        public void setStatisticsTime(LocalDateTime statisticsTime) { this.statisticsTime = statisticsTime; }
    }
}