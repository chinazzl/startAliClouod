package com.alicloud.dao.mapper;

import com.alicloud.dao.bean.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据用户ID查询权限列表
     */
    @Select("SELECT DISTINCT p.* FROM sys_permission p " +
            "LEFT JOIN sys_role_permission rp ON p.id = rp.permission_id " +
            "LEFT JOIN sys_user_role ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND p.status = 1 " +
            "ORDER BY p.sort_order")
    List<Permission> getPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询权限代码列表
     */
    @Select("SELECT DISTINCT p.permission_code FROM sys_permission p " +
            "LEFT JOIN sys_role_permission rp ON p.id = rp.permission_id " +
            "LEFT JOIN sys_user_role ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND p.status = 1")
    List<String> getPermissionCodesByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询权限列表
     */
    @Select("SELECT p.* FROM sys_permission p " +
            "INNER JOIN sys_role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId} AND p.status = 1 " +
            "ORDER BY p.sort_order")
    List<Permission> getPermissionsByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询所有权限（树形结构）
     */
    @Select("SELECT * FROM sys_permission WHERE status = 1 ORDER BY parent_id, sort_order")
    List<Permission> getAllPermissions();

    /**
     * 根据父权限ID查询子权限
     */
    @Select("SELECT * FROM sys_permission WHERE parent_id = #{parentId} AND status = 1 ORDER BY sort_order")
    List<Permission> getPermissionsByParentId(@Param("parentId") Long parentId);

}
