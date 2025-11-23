package com.alicloud.service;

import com.alicloud.api.bean.dto.UserLoginDto;
import com.alicloud.api.bean.vo.ModelVo;
import com.alicloud.common.model.AuthResponse;
import com.alicloud.common.model.UserVo;

import java.util.List;

/**********************************
 * @author zhang zhao lin
 * @date 2023年04月16日 13:17
 * @Description
 **********************************/
public interface UserService {

    /**
     *
     * @param username 用户名
     * @param password 密码
     * @param loginType 0：用户密码登录，1 手机号验证登录
     * @param loginSystem 登陆系统
     * @return
     */
    ModelVo login(String username, String password, int loginType, String loginSystem);

    /**
     * 获取所有的用户信息
     * @return
     */
    List<UserVo> getUserTotalList();

    ModelVo getJwtUserPubKey();

    /**
     * 注销接口
     * @return 是否注销成功
     */
    Boolean logout();
}
