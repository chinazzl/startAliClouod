package com.alicloud.service;

import com.alicloud.common.model.auth.PermissionTreeVO;
import com.alicloud.dao.bean.Permission;

import java.util.List;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description: 权限服务
 **/
public interface PermissionService {

    /**
     * 获取用户权限列表
     */
    List<Permission> getPermissionsByUserId(Long userId);

    /**
     * 获取用户权限代码列表
     */
    List<String> getPermissionsCodeByUserId(Long userId);

    /**
     * 检查用户是否有指定权限
     */
    boolean hasPermission(Long userId, String permissionCode);

    /**
     * 获取权限树形结构
     */
    List<PermissionTreeVO> getPermissionTree(Long userId);

}
