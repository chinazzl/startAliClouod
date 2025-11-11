package com.alicloud.dao.bean;

import com.alicloud.dao.config.mybatis.handler.CommonEnumTypeHandler;
import com.alicloud.common.enums.DelFlag;
import com.alicloud.common.enums.Sex;
import com.alicloud.common.enums.UserStatus;
import com.alicloud.common.enums.UserType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value = "sys_user", autoResultMap = true)
public class User {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "password")
    private String password;

    @TableField(value = "nick_name")
    private String nickName;

    @TableField(value = "type",typeHandler = CommonEnumTypeHandler.class,jdbcType = JdbcType.INTEGER)
    private UserType userType;

    @TableField(value = "status",typeHandler = CommonEnumTypeHandler.class,jdbcType = JdbcType.INTEGER)
    private UserStatus userStatus;

    @TableField(value = "phonenumber")
    private String phoneNumber;

    @TableField(value = "sex",typeHandler = CommonEnumTypeHandler.class,jdbcType = JdbcType.INTEGER)
    private Sex sex;

    @TableField(value = "avatar")
    private String avatar;

    @TableField(value = "create_by")
    private Long createBy;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "update_by")
    private Long updateBy;
    @TableField(value = "update_time")
    private Date updateTime;
    @TableField(value = "del_flag",typeHandler = CommonEnumTypeHandler.class,jdbcType = JdbcType.INTEGER)
    private DelFlag delFlag;
    private String email;

    @TableField(exist = false)
    private int loginType;
    @TableField(exist = false)
    private String mobile;

}
