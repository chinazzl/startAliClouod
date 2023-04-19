package com.alicloud.api.service.user;

import com.alicloud.api.service.BaseService;
import com.alicloud.api.vo.ModelVo;
import com.alicloud.model.User;

/**********************************
 * @author zhang zhao lin
 * @date 2023年04月16日 13:17
 * @Description
 **********************************/
public interface UserService extends BaseService {

    /**
     *
     * @param username 用户名
     * @param password 密码
     * @param loginType 0：用户密码登录，1 手机号验证登录
     * @param loginSystem 登陆系统
     * @return
     */
    ModelVo login(String username, String password, int loginType, String loginSystem);

    ModelVo getJwtUserPubKey();

}
