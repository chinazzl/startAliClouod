package com.alicloud.common.model;

import com.alicloud.common.enums.DelFlag;
import com.alicloud.common.enums.Sex;
import com.alicloud.common.enums.UserStatus;
import com.alicloud.common.enums.UserType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: zhaolin
 * @Date: 2024/12/4
 **/
@Data
public class UserVo implements Serializable {

    private Long id;

    private String userName;

    private String password;

    private String nickName;

    private UserType userType;

    private UserStatus userStatus;

    private String phoneNumber;

    private Sex sex;

    private String avatar;

    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private DelFlag delFlag;
    private String email;

    private int loginType;
    private String mobile;

    /**
     * 登录失败次数
     */
    private Integer loginFailCount;

    /**
     * 账户是否锁定
     */
    private Boolean accountLocked;

    /**
     * 锁定时间
     */
    private Date lockTime;

    /**
     * 解锁时间
     */
    private Date unlockTime;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 角色
     */
    private String role;
    /**
     * 权限集合
     */
    private String permission;
}
