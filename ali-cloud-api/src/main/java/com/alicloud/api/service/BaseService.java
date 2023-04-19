package com.alicloud.api.service;/**********************************
 * @author zhang zhao lin
 * @date 2023年04月16日 12:49
 * @Description:
 **********************************/

import com.alicloud.api.pojo.BaseEntity;
import com.alicloud.api.vo.ModelVo;

import java.io.Serializable;

/**
 * @author zhang zhao lin
 * @date 2023年04月16日 12:49
 * @Description
 */
public interface BaseService extends Serializable {

    /**
     *
     * @param username 用户名
     * @param password 密码
     * @param loginType 0：用户密码登录，1 手机号验证登录
     * @param loginSystem 登陆系统
     * @return
     */
    ModelVo login(String username, String password, int loginType, String loginSystem);
}
