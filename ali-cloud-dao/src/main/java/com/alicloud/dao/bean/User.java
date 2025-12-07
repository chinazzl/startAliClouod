package com.alicloud.dao.bean;

import com.alicloud.dao.config.mybatis.handler.CommonEnumTypeHandler;
import com.alicloud.dao.enums.DelFlag;
import com.alicloud.dao.enums.Sex;
import com.alicloud.dao.enums.UserStatus;
import com.alicloud.dao.enums.UserType;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 15:49
 * @Description:
 */
@Data
@EqualsAndHashCode
@TableName(value = "sys_user",autoResultMap = true)
public class User {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "password")
    private String password;

    @TableField(value = "nick_name")
    private String nickName;

    @TableField(value = "type",typeHandler = CommonEnumTypeHandler.class, javaType = true, jdbcType = JdbcType.TINYINT)
    private UserType userType;

    @TableField(value = "status",typeHandler = CommonEnumTypeHandler.class, javaType = true,jdbcType = JdbcType.TINYINT)
    private UserStatus userStatus;

    @TableField(value = "email")
    private String email;

    @TableField(value = "phonenumber")
    private String phoneNumber;

    @TableField(value = "sex",typeHandler = CommonEnumTypeHandler.class, javaType = true,jdbcType = JdbcType.TINYINT)
    private Sex sex;

    @TableField(value = "avatar")
    private String avatar;

    @TableField(value = "create_by")
    private Long createBy;
    @TableField(value = "create_time",fill = FieldFill.INSERT,jdbcType = JdbcType.TIMESTAMP)
    private Date createTime;
    @TableField(value = "update_by")
    private Long updateBy;
    @TableField(value = "update_time",fill = FieldFill.UPDATE,jdbcType = JdbcType.TIMESTAMP)
    private Date updateTime;
    @TableField(value = "del_flag",typeHandler = CommonEnumTypeHandler.class,javaType = true, jdbcType = JdbcType.TINYINT)
    private DelFlag delFlag;

    @TableField(exist = false)
    private int loginType;
    @TableField(exist = false)
    private String mobile;

    @TableField("account_non_expired")
    private boolean accountNonExpired;

    @TableField("account_locked")
    private boolean accountLocked;

    @TableField("credentials_non_expired")
    private boolean credentialsNonExpired;
    private boolean enabled;
    @TableField(value = "password_salt")
    private String passwordSalt;
    @TableField(value = "last_login_ip")
    private String lastLoginIp;
    @TableField("password_update_time")
    private Date passwordUpdateTime;

    @TableField(value = "login_fail_count")
    private Integer loginFailCount;

    @TableField(value = "lock_time")
    private Date lockTime;

    @TableField(value = "unlock_time")
    private Date unlockTime;

    @TableField(value = "last_login_time")
    private Date lastLoginTime;

}
