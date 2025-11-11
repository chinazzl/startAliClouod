package com.alicloud.service.collect.impl;

import com.alicloud.common.enums.DataStatus;
import com.alicloud.service.collect.DataExpireService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据过期处理服务实现类
 */
@Slf4j
@Service
public class DataExpireServiceImpl implements DataExpireService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public int markExpiredData(int expireThreshold) {
        log.info("开始标记过期数据，过期阈值: {} 天", expireThreshold);

        String sql = String.format(
            "UPDATE target_table SET data_status = %d, update_time = NOW() " +
            "WHERE data_status = %d AND last_update_time < DATE_SUB(NOW(), INTERVAL %d DAY)",
            DataStatus.EXPIRED.getCode(),
            DataStatus.NORMAL.getCode(),
            expireThreshold
        );

        int updatedCount = jdbcTemplate.update(sql);
        log.info("标记过期数据完成，影响记录数: {}", updatedCount);

        return updatedCount;
    }

    @Override
    public int markExpiredDataByStatus(DataStatus currentStatus, int expireThreshold) {
        log.info("开始标记过期数据，当前状态: {}, 过期阈值: {} 天", currentStatus, expireThreshold);

        String sql = String.format(
            "UPDATE target_table SET data_status = %d, update_time = NOW() " +
            "WHERE data_status = %d AND last_update_time < DATE_SUB(NOW(), INTERVAL %d DAY)",
            DataStatus.EXPIRED.getCode(),
            currentStatus.getCode(),
            expireThreshold
        );

        int updatedCount = jdbcTemplate.update(sql);
        log.info("标记过期数据完成，影响记录数: {}", updatedCount);

        return updatedCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteExpiredData(int batchSize) {
        log.info("开始删除已过期数据，批次大小: {}", batchSize);

        int totalDeletedCount = 0;

        while (true) {
            // 查询一批要删除的数据ID
            String selectSql = String.format(
                "SELECT id FROM target_table " +
                "WHERE data_status = %d " +
                "LIMIT %d",
                DataStatus.TO_DELETE.getCode(),
                batchSize
            );

            List<String> dataIds = jdbcTemplate.queryForList(selectSql, String.class);

            if (dataIds.isEmpty()) {
                break;
            }

            // 删除数据
            String deleteSql = "DELETE FROM table WHERE id = ? ";
            int deletedCount = 0;

            for (String dataId : dataIds) {
                try {
                    deletedCount += jdbcTemplate.update(deleteSql, dataId);
                } catch (Exception e) {
                    log.error("删除数据失败: {}, 异常: {}", dataId, e.getMessage(), e);
                }
            }

            totalDeletedCount += deletedCount;
            log.info("本次删除数据: {} 条，累计删除: {} 条", deletedCount, totalDeletedCount);

            // 如果删除的数据量小于批次大小，说明已经删除完毕
            if (deletedCount < batchSize) {
                break;
            }
        }

        log.info("删除过期数据完成，总计删除: {} 条", totalDeletedCount);
        return totalDeletedCount;
    }

    @Override
    public ExpiredDataStatistics getExpiredDataStatistics() {
        String sql = String.format(
            "SELECT " +
            "SUM(CASE WHEN data_status = %d THEN 1 ELSE 0 END) as normal_count, " +
            "SUM(CASE WHEN data_status = %d THEN 1 ELSE 0 END) as expired_count, " +
            "SUM(CASE WHEN data_status = %d THEN 1 ELSE 0 END) as to_delete_count " +
            "FROM target_table",
            DataStatus.NORMAL.getCode(),
            DataStatus.EXPIRED.getCode(),
            DataStatus.TO_DELETE.getCode()
        );

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                ExpiredDataStatistics statistics = new ExpiredDataStatistics();
                statistics.setNormalCount(rs.getLong("normal_count"));
                statistics.setExpiredCount(rs.getLong("expired_count"));
                statistics.setToDeleteCount(rs.getLong("to_delete_count"));
                statistics.setStatisticsTime(LocalDateTime.now());
                return statistics;
            });
        } catch (Exception e) {
            log.error("获取过期数据统计失败: {}", e.getMessage(), e);
            return createEmptyStatistics();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDataStatus(List<String> dataIds, DataStatus fromStatus, DataStatus toStatus) {
        if (dataIds == null || dataIds.isEmpty()) {
            return 0;
        }

        log.info("开始批量更新数据状态，数量: {}, 从: {} 到: {}", dataIds.size(), fromStatus, toStatus);

        String sql = String.format(
            "UPDATE target_table SET data_status = %d, update_time = NOW() " +
            "WHERE data_status = %d AND id IN (%s)",
            toStatus.getCode(),
            fromStatus.getCode(),
            String.join(",", dataIds.stream().map(id -> "'" + id + "'").collect(Collectors.toList()))
        );

        int updatedCount = jdbcTemplate.update(sql);
        log.info("批量更新数据状态完成，影响记录数: {}", updatedCount);

        return updatedCount;
    }

    /**
     * 创建空的统计信息
     * @return 空的统计对象
     */
    private ExpiredDataStatistics createEmptyStatistics() {
        ExpiredDataStatistics statistics = new ExpiredDataStatistics();
        statistics.setNormalCount(0);
        statistics.setExpiredCount(0);
        statistics.setToDeleteCount(0);
        statistics.setStatisticsTime(LocalDateTime.now());
        return statistics;
    }
}