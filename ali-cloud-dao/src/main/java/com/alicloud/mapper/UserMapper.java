package com.alicloud.mapper;

import com.alicloud.bean.Menu;
import com.alicloud.bean.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 15:54
 * @Description: 用户模块
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User getUserToLogin(User user);
    List<User> getUserList();

    List<Menu> getUserPermission(@Param("userId") Long userId);
}
