-- 为sys_user表添加登录安全相关字段
-- 执行前请备份数据库

-- 添加登录失败次数字段
ALTER TABLE sys_user ADD COLUMN login_fail_count INT DEFAULT 0 COMMENT '登录失败次数';

-- 添加账户锁定状态字段
ALTER TABLE sys_user ADD COLUMN account_locked TINYINT(1) DEFAULT 0 COMMENT '账户是否锁定 0-未锁定 1-已锁定';

-- 添加锁定时间字段
ALTER TABLE sys_user ADD COLUMN lock_time DATETIME NULL COMMENT '锁定时间';

-- 添加解锁时间字段
ALTER TABLE sys_user ADD COLUMN unlock_time DATETIME NULL COMMENT '解锁时间';

-- 添加最后登录时间字段
ALTER TABLE sys_user ADD COLUMN last_login_time DATETIME NULL COMMENT '最后登录时间';

-- 创建索引提高查询性能
CREATE INDEX idx_user_account_locked ON sys_user(account_locked);
CREATE INDEX idx_user_lock_time ON sys_user(lock_time);
CREATE INDEX idx_user_unlock_time ON sys_user(unlock_time);