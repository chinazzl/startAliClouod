package com.alicloud.service.impl.user;

import com.alicloud.common.config.LoginSecurityConfig;
import com.alicloud.common.exception.AccountLockedException;
import com.alicloud.common.model.LoginFailRecord;
import com.alicloud.dao.bean.User;
import com.alicloud.dao.mapper.UserMapper;
import com.alicloud.service.LoginFailService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 登录失败处理服务实现
 * @author Claude
 * @date 2025-11-18
 */
@Slf4j
@Service
public class LoginFailServiceImpl implements LoginFailService {

    private static final String LOGIN_FAIL_KEY_PREFIX = "login:fail:";

    @Autowired
    private LoginSecurityConfig loginSecurityConfig;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public LoginFailRecord recordLoginFail(String userName, String ipAddress, String userAgent) {
        if (!loginSecurityConfig.isEnableLoginFailLimit()) {
            return null;
        }

        try {
            // 查询用户信息
            User user = userMapper.getUserByName(userName);
            if (user == null) {
                log.warn("用户不存在: {}", userName);
                return null;
            }

            // 更新数据库中的失败次数
            int failCount = (user.getLoginFailCount() == null ? 0 : user.getLoginFailCount()) + 1;
            user.setLoginFailCount(failCount);
            user.setLastLoginTime(new Date());

            // 检查是否需要锁定账户
            boolean shouldLock = failCount >= loginSecurityConfig.getMaxFailCount();
            if (shouldLock && user.isAccountLocked()) {
                user.setAccountLocked(true);
                user.setLockTime(new Date());

                // 计算解锁时间
                long lockDuration = loginSecurityConfig.getLockDurationMinutes();
                long unlockTimeMillis = System.currentTimeMillis() + lockDuration * 60 * 1000;
                user.setUnlockTime(new Date(unlockTimeMillis));

                log.warn("用户账户已被锁定: {}, 失败次数: {}, 锁定时长: {}分钟",
                    userName, failCount, lockDuration);
            }

            userMapper.updateById(user);

            // 记录Redis信息
            String redisKey = LOGIN_FAIL_KEY_PREFIX + userName;
            stringRedisTemplate.opsForValue().set(redisKey, String.valueOf(failCount),
                loginSecurityConfig.getRecordExpireHours(), TimeUnit.HOURS);

            // 构建返回对象
            LoginFailRecord record = new LoginFailRecord();
            record.setUserId(user.getId());
            record.setUserName(userName);
            record.setFailCount(failCount);
            record.setLastFailTime(new Date());
            record.setIpAddress(ipAddress);
            record.setUserAgent(userAgent);
            record.setLockTime(user.getLockTime());
            record.setUnlockTime(user.getUnlockTime());
            record.setIsLocked(user.isAccountLocked());

            // 如果账户被锁定，抛出异常
            if (shouldLock) {
                throw new AccountLockedException(String.format(
                    "账户已被锁定，连续登录失败%d次，请在%d分钟后重试",
                    failCount, loginSecurityConfig.getLockDurationMinutes()));
            }

            return record;

        } catch (AccountLockedException e) {
            throw e;
        } catch (Exception e) {
            log.error("记录登录失败信息异常: {}", userName, e);
            return null;
        }
    }

    @Override
    @Transactional
    public void clearLoginFail(String userName) {
        try {
            // 清除数据库记录
            User user = userMapper.getUserByName(userName);
            if (user != null) {
                user.setLoginFailCount(0);
                user.setLastLoginTime(new Date());
                // 如果账户被锁定但锁定时间已过，则解锁
                if (user.isAccountLocked() && user.getUnlockTime() != null) {
                    if (new Date().after(user.getUnlockTime())) {
                        user.setAccountLocked(false);
                        user.setLockTime(null);
                        user.setUnlockTime(null);
                        log.info("用户账户自动解锁: {}", userName);
                    }
                }
                userMapper.updateById(user);
            }

            // 清除Redis记录
            String redisKey = LOGIN_FAIL_KEY_PREFIX + userName;
            stringRedisTemplate.delete(redisKey);

            log.info("清除登录失败记录: {}", userName);
        } catch (Exception e) {
            log.error("清除登录失败记录异常: {}", userName, e);
        }
    }

    @Override
    public boolean isAccountLocked(String userName) {
        try {
            User user = userMapper.getUserByName(userName);
            if (user == null) {
                return false;
            }

            // 检查账户锁定状态
            if (user.isAccountLocked()) {
                // 检查是否已过锁定时间
                if (user.getUnlockTime() != null && new Date().after(user.getUnlockTime())) {
                    // 自动解锁账户
                    clearLoginFail(userName);
                    return false;
                }
                return true;
            }

            return false;
        } catch (Exception e) {
            log.error("检查账户锁定状态异常: {}", userName, e);
            return false;
        }
    }

    @Override
    public LoginFailRecord getLoginFailRecord(String userName) {
        try {
            User user = userMapper.getUserByName(userName);
            if (user == null) {
                return null;
            }

            LoginFailRecord record = new LoginFailRecord();
            BeanUtils.copyProperties(user, record);
            record.setFailCount(user.getLoginFailCount() == null ? 0 : user.getLoginFailCount());
            record.setIsLocked(user.isAccountLocked());

            return record;
        } catch (Exception e) {
            log.error("获取登录失败记录异常: {}", userName, e);
            return null;
        }
    }

    @Override
    @Transactional
    public void unlockAccount(String userName) {
        try {
            User user = userMapper.getUserByName(userName);
            if (user != null) {
                user.setLoginFailCount(0);
                user.setAccountLocked(false);
                user.setLockTime(null);
                user.setUnlockTime(null);
                userMapper.updateById(user);

                // 清除Redis记录
                String redisKey = LOGIN_FAIL_KEY_PREFIX + userName;
                stringRedisTemplate.delete(redisKey);

                log.info("管理员手动解锁账户: {}", userName);
            }
        } catch (Exception e) {
            log.error("解锁账户异常: {}", userName, e);
        }
    }
}