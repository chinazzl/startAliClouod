package com.alicloud.common.utils;

import com.alicloud.common.config.security.LoginUser;
import com.alicloud.common.model.UserVo;
import com.google.common.collect.Lists;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author: zhaolin
 * @Date: 2025/11/22
 * @Description: 获取当前登录用户
 **/
public class SecuritytUtils {

    public static LoginUser getCurrentLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser) {
            return (LoginUser) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 获取当前用户信息
     * @return
     */
    public static UserVo getCurrentUser() {
        LoginUser currentLoginUser = getCurrentLoginUser();
        return currentLoginUser!= null ? currentLoginUser.getUserVo() : null;
    }

    /**
     * 获取当前用户ID
     * @return
     */
    public static Long getCurrentUserId() {
        UserVo currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getId() : null;
    }

    /**
     * 获取当前用户名字
     * @return
     */
    public static String getCurrentUserName() {
        UserVo currentUser = getCurrentUser();
        return currentUser!= null ? currentUser.getUserName() : null;
    }

    /**
     * 获取当前用户角色列表
     */
    public static List<String> getCurrentUserRoles() {
        LoginUser loginUser = getCurrentLoginUser();
        return loginUser != null ? loginUser.getRoles() : null;
    }

    /**
     * 获取当前用户权限列表
     */
    public static List<String> getCurrentUserPermissions() {
        LoginUser loginUser = getCurrentLoginUser();
        return loginUser != null ? loginUser.getPermissions() : null;
    }

    /**
     * 检查当前用户是否已经认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    /**
     * 检查当前用户是否具有指定的权限
     */
    public static boolean hasPermission(String permission) {
        if (!isAuthenticated()) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = Objects.requireNonNull(getCurrentLoginUser()).getAuthorities();
        return authorities.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(permission));
    }

    /**
     * 检查当前用户是否具有任意一个指定的权限
     */
    public static boolean hasRole(String... permissions) {
        if (!isAuthenticated()) {
            return false;
        }
        List<String> permissionsList = List.of(permissions);
        Collection<? extends GrantedAuthority> authorities = Objects.requireNonNull(getCurrentLoginUser()).getAuthorities();
        return authorities.stream().map(GrantedAuthority::getAuthority).anyMatch(permissionsList:: contains);
    }

}
