package com.alicloud.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: zhaolin
 * @Date: 2024/12/14
 **/
@TableName("sys_menu")
@Data
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("menu_name")
    private String menuName;

    /**
     * 父菜单ID
     */
    @TableField("parent_id")
    private Long parentId;

    @TableField("order_num")
    private Integer orderNum;

    @TableField("path")
    private String path;

    @TableField("component")
    private String component;

    @TableField("is_frame")
    private Integer isFrame;

    @TableField("menu_type")
    private Integer menuType;

    @TableField("visible")
    private Integer visible;

    @TableField("status")
    private Integer status;

    @TableField("perms")
    private String perms;

    @TableField("permission_code")
    private String permissionCode;

    @TableField("permission_type")
    private Integer permissionType;

    @TableField("api_url")
    private String apiUrl;

    @TableField("request_method")
    private String requestMethod;

    @TableField("icon")
    private String icon;

    @TableField("keep_alive")
    private Integer keepAlive;

    @TableField("always_show")
    private Integer alwaysShow;

    // 非持久化字段
    @TableField(exist = false)
    private List<Menu> children;

    @TableField(exist = false)
    private String roleKey;
}
