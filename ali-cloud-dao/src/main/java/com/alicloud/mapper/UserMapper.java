package com.alicloud.mapper;

import com.alicloud.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 15:54
 * @Description:
 */
@Repository
public interface UserMapper {

    List<User> getUserList();
}
