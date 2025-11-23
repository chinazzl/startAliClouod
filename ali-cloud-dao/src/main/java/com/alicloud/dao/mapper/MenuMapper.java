package com.alicloud.dao.mapper;

import com.alicloud.dao.bean.Menu;
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
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户ID查询菜单权限
     */
    @Select("SELECT DISTINCT m.* FROM sys_menu m " +
            "LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "LEFT JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.status = 0 " +
            "ORDER BY m.parent_id, m.order_num")
    List<Menu> getMenusByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询权限代码
     */
    @Select("SELECT DISTINCT m.permission_code FROM sys_menu m " +
            "LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "LEFT JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.status = 0 AND m.permission_code IS NOT NULL")
    List<String> getPermissionCodesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询接口权限
     */
    @Select("SELECT DISTINCT m.api_url, m.request_method FROM sys_menu m " +
            "LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "LEFT JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.status = 0 AND m.permission_type = 4 AND m.api_url IS NOT NULL")
    List<Menu> getApiPermissionsByUserId(@Param("userId") Long userId);
}
