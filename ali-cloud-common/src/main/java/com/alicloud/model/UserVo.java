package com.alicloud.model;

import com.alicloud.enums.DelFlag;
import com.alicloud.enums.Sex;
import com.alicloud.enums.UserStatus;
import com.alicloud.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
}
