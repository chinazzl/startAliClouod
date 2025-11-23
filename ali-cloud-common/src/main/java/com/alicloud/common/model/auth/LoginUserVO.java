package com.alicloud.common.model.auth;

import com.alicloud.common.model.UserVo;
import lombok.Data;

import java.util.List;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
@Data
public class LoginUserVO {

    private UserVo userVo;

    private List<String> roles;

    private List<String> permissions;
}
