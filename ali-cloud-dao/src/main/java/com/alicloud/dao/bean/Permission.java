package com.alicloud.dao.bean;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
@Data
@EqualsAndHashCode
@TableName(value = "sys_permission", autoResultMap = true)
public class Permission {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父权限ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 权限名称
     */
    @TableField("permission_name")
    private String permissionName;

    /**
     * 权限标识
     */
    @TableField("permission_code")
    private String permissionCode;

    /**
     * 权限类型
     */
    @TableField("permission_type")
    private Integer permissionType;

    /**
     * 路由地址
     */
    @TableField("path")
    private String path;

    /**
     * 组件路径
     */
    @TableField("component")
    private String component;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 子权限列表（非持久化字段）
     */
    @TableField(exist = false)
    private List<Permission> children;

    /**
     * 权限级别
     */
    @TableField(exist = false)
    private Integer level;


}
