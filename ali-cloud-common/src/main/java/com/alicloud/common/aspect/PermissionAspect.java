package com.alicloud.common.aspect;

import com.alicloud.common.annotation.RequirePermission;
import com.alicloud.common.exception.UnauthorizedException;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
@Aspect
@Slf4j
@Component
public class PermissionAspect {


    @Before("@annotation(requirePermission)")
    public void checkPermission(JoinPoint joinPoint, RequirePermission requirePermission) {
        try {
            //  TODO 获取当前用户 从上下文中获取
//            Long currentUserId = SecuritytUtils.getCurrentUserId();
            Long currentUserId = null;
            if (currentUserId == null) {
                throw new UnauthorizedException("用户未登录");
            }

            // TODO 获取用户权限
//            List<String> userPermissions = SecuritytUtils.getCurrentUserPermissions();
            List<String> userPermissions = Lists.newArrayList();
            if (userPermissions == null || userPermissions.isEmpty()) {
                throw new UnauthorizedException("用户无任何权限");
            }

            // 检查权限
            String[] requiredPermissions = requirePermission.value();
            RequirePermission.Logical logical = requirePermission.logical();

            boolean hasPermission = checkPermissions(userPermissions, requiredPermissions, logical);

            if (!hasPermission) {
                String errorMsg = String.format("权限不足，需要权限: %s", Arrays.toString(requiredPermissions));
                log.warn("权限验证失败: userId={}, requiredPermissions={}",
                        currentUserId, Arrays.toString(requiredPermissions));
                throw new UnauthorizedException(errorMsg);
            }

        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            log.error("权限验证异常", e);
            throw new UnauthorizedException("权限验证失败",e);
        }
    }

    private boolean checkPermissions(List<String> userPermissions,
                                     String[] requiredPermissions,
                                     RequirePermission.Logical logical) {
        if (requiredPermissions == null || requiredPermissions.length == 0) {
            return true;
        }

        if (logical == RequirePermission.Logical.AND) {
            // 所有权限都必须具备
            return Arrays.stream(requiredPermissions)
                    .allMatch(userPermissions::contains);
        } else {
            // 只需具备任一权限
            return Arrays.stream(requiredPermissions)
                    .anyMatch(userPermissions::contains);
        }
    }

}
