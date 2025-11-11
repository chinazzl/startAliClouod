package com.alicloud.common.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author: zhaolin
 * @Date: 2024/12/15
 * @Description: 自定义权限解析
 **/
@Component("customAuth")
public class CustomSecurityExpression {

    public boolean hasPermission(String permission) {
        // 1. 获取当前系统中所有的权限信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        if (loginUser == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = loginUser.getAuthorities();
        // 2. 将权限信息与调用接口需要的权限进行比对，如果存在则true，如果不存在则返回false
       return authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(permission));

    }
}
