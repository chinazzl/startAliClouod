package com.alicloud.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: zhaolin
 * @Date: 2024/12/14
 **/
@TableName("sys_menu")
@Data
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名称
     */
    @TableField(value = "name")
    private String menuName;

    /**
     * 父菜单ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 显示顺序
     */
    @TableField("sort")
    private Integer orderNum;

    /**
     * 菜单类型
     */
    @TableField("type")
    private Integer type;
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
     * 组件名称
     */
    @TableField("component_name")
    private String componentName;

    /**
     * 是否为外链（0是 1否）
     */
    @TableField(exist = false)
    private Integer isFrame;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @TableField(exist = false)
    private String menuType;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    @TableField(value = "visible")
    private Boolean visible;

    /**
     * 菜单状态（0正常 1停用）
     */
    @TableField("status")
    private Integer status;

    /**
     * 是否缓存
     */
    @TableField("keep_alive")
    private Boolean keepAlive;

    /**
     * 是否总是显示
     */
    @TableField("always_show")
    private Boolean alwaysShow;

    /**
     * 权限标识
     */
    @TableField("perms")
    private String perms;

    /**
     * 菜单图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 创建者
     */
    @TableField("creator")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @TableField("updater")
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 是否删除
     */
    @TableField("deleted")
    private Boolean delFlag;

    @TableField("role_key")
    private String roleKey;
}
