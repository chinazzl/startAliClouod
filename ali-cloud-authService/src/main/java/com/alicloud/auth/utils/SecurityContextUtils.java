package com.alicloud.auth.utils;

import com.alicloud.auth.config.LoginUser;
import com.alicloud.common.utils.IpUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhaolin
 * @Date: 2025/11/22
 * @Description: 获取当前登录用户
 **/
public class SecurityContextUtils {

    /**
     * 获取当前登录用户
     */
    public static LoginUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser) {
            return (LoginUser) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        LoginUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getUserVo().getId() : null;
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUsername() {
        LoginUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getUserVo().getUserName() : null;
    }

    /**
     * 获取当前用户权限列表
     */
    public static List<String> getCurrentUserPermissions() {
        LoginUser currentUser = getCurrentUser();
        if (currentUser != null) {
            Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
            return authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 检查当前用户是否有指定权限
     */
    public static boolean hasPermission(String permission) {
        List<String> permissions = getCurrentUserPermissions();
        return permissions != null && permissions.contains(permission);
    }

    /**
     * 检查当前用户是否有任意一个指定权限
     */
    public static boolean hasAnyPermission(String... permissions) {
        List<String> userPermissions = getCurrentUserPermissions();
        if (userPermissions == null) {
            return false;
        }
        return Arrays.stream(permissions)
                .anyMatch(userPermissions::contains);
    }

    /**
     * 检查当前用户是否有所有指定权限
     */
    public static boolean hasAllPermissions(String... permissions) {
        List<String> userPermissions = getCurrentUserPermissions();
        if (userPermissions == null) {
            return false;
        }
        return Arrays.stream(permissions)
                .allMatch(userPermissions::contains);
    }

    /**
     * 检查当前用户是否已认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * 检查当前用户是否为管理员
     */
    public static boolean isAdmin() {
        return hasPermission("ROLE_ADMIN");
    }

    /**
     * 获取当前用户IP地址
     */
    public static String getCurrentUserIp() {
        // 可以从RequestContext或其他地方获取
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return IpUtils.getIpAddress(requestAttributes.getRequest());
    }

}
