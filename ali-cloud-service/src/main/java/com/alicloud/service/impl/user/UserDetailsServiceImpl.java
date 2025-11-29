package com.alicloud.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.alicloud.auth.config.LoginUser;
import com.alicloud.common.model.UserVo;
import com.alicloud.dao.bean.Menu;
import com.alicloud.dao.bean.User;
import com.alicloud.dao.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: zhaolin
 * @Date: 2024/12/8
 **/
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    UserMapper userMapper;
    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户不存在，username : " + userName);
        }
        UserVo userVo = BeanUtil.copyProperties(user, UserVo.class);
        // 增加鉴权，正常认证是存储在数据库中
//        List<String> role = Lists.newArrayList("test1", "dev");
        List<Menu> userPermission = userMapper.getUserPermission(userVo.getId());
        List<String> permissions = userPermission.stream().map(Menu::getPerms).filter(StringUtils::isNoneBlank).collect(Collectors.toList());
        List<String> roles = userPermission.stream().map(Menu::getRoleKey).filter(StringUtils::isNoneBlank).collect(Collectors.toList());
        return new LoginUser(userVo,roles,permissions);
    }
}
