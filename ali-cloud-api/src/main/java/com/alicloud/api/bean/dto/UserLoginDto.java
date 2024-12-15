package com.alicloud.api.bean.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhaolin
 * @Date: 2024/12/9
 **/
@Data
public class UserLoginDto implements Serializable {

    private String username;
    private String password;
}
