package com.alicloud.web.hystrix;

import com.alicloud.model.User;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**********************************
 * @author zhang zhao lin
 * @date 2022年09月28日 22:31
 * @Description: 远程调用回滚
 **********************************/
public class UserServiceFallbackFactory implements FallbackFactory<UserRemote> {
    Logger log = LoggerFactory.getLogger(UserServiceFallbackFactory.class);

    @Override
    public UserRemote create(Throwable throwable) {
        return new UserRemote() {
            @Override
            public Integer getUserToLogin(User user) {
                log.error("用户服务验证失败");
                return null;
            }

            @Override
            public List<User> getUserList() {
                log.error("请求超时");
                return null;
            }
        };
    }

}
