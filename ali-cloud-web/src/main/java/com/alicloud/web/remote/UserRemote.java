package com.alicloud.web.remote;

import com.alicloud.model.User;
import com.alicloud.web.hystrix.UserServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**********************************
 * @author zhang zhao lin
 * @date 2022年09月28日 22:28
 * @Description:
 **********************************/
@FeignClient(name = "ali-cloud-service",fallbackFactory = UserServiceFallbackFactory.class)
public interface UserRemote {

    @RequestMapping("/userService/selectAllUser")
    List<User> getUserList();
}
