-- op_db.sys_menu definition
-- 菜单权限
CREATE TABLE `sys_menu` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
                            `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
                            `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
                            `order_num` int DEFAULT '0' COMMENT '显示顺序',
                            `path` varchar(200) DEFAULT '' COMMENT '路由地址',
                            `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
                            `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
                            `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
                            `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
                            `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
                            `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
                            `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
                            `create_by` bigint DEFAULT NULL COMMENT '创建者',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` bigint DEFAULT NULL COMMENT '更新者',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `remark` varchar(500) DEFAULT '' COMMENT '备注',
                            `del_flag` char(1) DEFAULT '0',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限表';

-- 用户角色关系
-- op_db.sys_user_role definition

CREATE TABLE `sys_user_role` (
                                 `user_id` bigint NOT NULL COMMENT '用户ID',
                                 `role_id` bigint NOT NULL COMMENT '角色ID',
                                 PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户和角色关联表';


-- 角色表
-- op_db.sys_role definition

CREATE TABLE `sys_role` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
                            `role_name` varchar(30) NOT NULL COMMENT '角色名称',
                            `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
                            `role_sort` int NOT NULL COMMENT '显示顺序',
                            `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
                            `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
                            `create_by` bigint DEFAULT NULL COMMENT '创建者',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` bigint DEFAULT NULL COMMENT '更新者',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色信息表';

-- 菜单角色关系
-- op_db.sys_role_menu definition

CREATE TABLE `sys_role_menu` (
                                 `role_id` bigint NOT NULL COMMENT '角色ID',
                                 `menu_id` bigint NOT NULL COMMENT '菜单ID',
                                 PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和菜单关联表';

-- 用户
-- op_db.sys_user definition

CREATE TABLE `sys_user` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                            `user_name` varchar(64) NOT NULL COMMENT '用户名',
                            `nick_name` varchar(64) NOT NULL COMMENT '昵称',
                            `password` varchar(64) NOT NULL COMMENT '密码',
                            `type` char(1) DEFAULT '0' COMMENT '用户类型：0代表普通用户，1代表管理员',
                            `status` char(1) DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
                            `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
                            `phonenumber` varchar(32) DEFAULT NULL COMMENT '手机号',
                            `sex` char(1) DEFAULT NULL COMMENT '用户性别（0男，1女，2未知）',
                            `avatar` varchar(128) DEFAULT NULL COMMENT '头像',
                            `create_by` bigint DEFAULT NULL COMMENT '创建人的用户id',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` bigint DEFAULT NULL COMMENT '更新人',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `del_flag` int DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

ALTER TABLE op_db.sys_user ADD login_fail_count INT DEFAULT 0 NULL;
ALTER TABLE op_db.sys_user ADD lock_time TIMESTAMP NULL;
ALTER TABLE op_db.sys_user ADD unlock_time TIMESTAMP NULL;
ALTER TABLE op_db.sys_user ADD last_login_time TIMESTAMP NULL;



-- 修改后的sys_menu表结构
ALTER TABLE `sys_menu`
    ADD COLUMN `permission_code` varchar(100) DEFAULT NULL COMMENT '权限代码' AFTER `perms`,
    ADD COLUMN `permission_type` tinyint(1) DEFAULT 1 COMMENT '权限类型(1:菜单 2:按钮 3:接口)' AFTER `permission_code`,
    ADD COLUMN `api_url` varchar(500) DEFAULT NULL COMMENT '接口URL(用于接口权限控制)' AFTER `component`,
    ADD COLUMN `request_method` varchar(10) DEFAULT NULL COMMENT '请求方法(GET/POST/PUT/DELETE)' AFTER `api_url`,
    ADD COLUMN `keep_alive` char(1) DEFAULT '0' COMMENT '是否缓存(0是 1否)' AFTER `visible`,
    ADD COLUMN `always_show` char(1) DEFAULT '0' COMMENT '是否总是显示(0否 1是)' AFTER `keep_alive`,
    MODIFY COLUMN `menu_type` tinyint(1) DEFAULT 1 COMMENT '菜单类型(1:目录 2:菜单 3:按钮)',
    MODIFY COLUMN `is_frame` tinyint(1) DEFAULT 0 COMMENT '是否为外链(0否 1是)',
    MODIFY COLUMN `visible` tinyint(1) DEFAULT 0 COMMENT '是否显示(0显示 1隐藏)',
    MODIFY COLUMN `status` tinyint(1) DEFAULT 0 COMMENT '状态(0正常 1停用)';

-- 添加索引优化查询性能
CREATE INDEX `idx_permission_code` ON `sys_menu`(`permission_code`);
CREATE INDEX `idx_permission_type` ON `sys_menu`(`permission_type`);
CREATE INDEX `idx_parent_id` ON `sys_menu`(`parent_id`);




-- 权限表（sys_permission）
CREATE TABLE `sys_permission` (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限ID',
                                  `parent_id` bigint DEFAULT '0' COMMENT '父权限ID',
                                  `permission_name` varchar(100) NOT NULL COMMENT '权限名称',
                                  `permission_code` varchar(100) NOT NULL COMMENT '权限标识',
                                  `permission_type` tinyint DEFAULT '1' COMMENT '权限类型(1:菜单 2:按钮)',
                                  `path` varchar(200) DEFAULT NULL COMMENT '路由地址',
                                  `component` varchar(200) DEFAULT NULL COMMENT '组件路径',
                                  `icon` varchar(100) DEFAULT '#' COMMENT '图标',
                                  `sort_order` int DEFAULT '0' COMMENT '排序',
                                  `status` tinyint DEFAULT '1' COMMENT '状态(1:正常 0:禁用)',
                                  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `uk_permission_code` (`permission_code`),
                                  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 角色权限关联表（sys_role_permission）
CREATE TABLE `sys_role_permission` (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                       `role_id` bigint NOT NULL COMMENT '角色ID',
                                       `permission_id` bigint NOT NULL COMMENT '权限ID',
                                       `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`),
                                       KEY `idx_role_id` (`role_id`),
                                       KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 用户权限关联表（sys_user_permission）
CREATE TABLE `sys_user_permission` (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                       `user_id` bigint NOT NULL COMMENT '用户ID',
                                       `permission_id` bigint NOT NULL COMMENT '权限ID',
                                       `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `uk_user_permission` (`user_id`,`permission_id`),
                                       KEY `idx_user_id` (`user_id`),
                                       KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户权限关联表';

-- 更新sys_user表，添加验证相关字段
ALTER TABLE sys_user
    ADD COLUMN `account_non_expired` tinyint(1) DEFAULT 1 COMMENT '账户是否未过期',
    ADD COLUMN `account_non_locked` tinyint(1) DEFAULT 1 COMMENT '账户是否未锁定',
    ADD COLUMN `credentials_non_expired` tinyint(1) DEFAULT 1 COMMENT '密码是否未过期',
    ADD COLUMN `enabled` tinyint(1) DEFAULT 1 COMMENT '账户是否启用',
    ADD COLUMN `password_salt` varchar(64) DEFAULT NULL COMMENT '密码盐值',
    ADD COLUMN `last_login_ip` varchar(64) DEFAULT NULL COMMENT '最后登录IP',
    ADD COLUMN `password_update_time` datetime DEFAULT NULL COMMENT '密码更新时间';

-- 初始化数据
INSERT INTO `sys_permission` (`permission_name`, `permission_code`, `permission_type`, `path`, `sort_order`) VALUES
                                                                                                                 ('系统管理', 'system', 1, '/system', 1),
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`)
SELECT 1, id FROM sys_permission WHERE status = 1;