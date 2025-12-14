package com.alicloud.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alicloud.common.model.auth.PermissionTreeVO;
import com.alicloud.dao.bean.Permission;
import com.alicloud.dao.mapper.PermissionMapper;
import com.alicloud.service.PermissionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> getPermissionsByUserId(Long userId) {
        return permissionMapper.getPermissionsByUserId(userId);
    }

    @Override
    public List<String> getPermissionsCodeByUserId(Long userId) {
        return permissionMapper.getPermissionCodesByUserId(userId);
    }

    @Override
    public boolean hasPermission(Long userId, String permissionCode) {
        List<String> codes = permissionMapper.getPermissionCodesByUserId(userId);
        return codes != null && codes.contains(permissionCode);
    }

    @Override
    public List<PermissionTreeVO> getPermissionTree(Long userId) {
        List<Permission> permissions = permissionMapper.getPermissionsByUserId(userId);
        return buildPermissionTree(permissions);
    }

    /**
     * 构建权限树
     */
    private List<PermissionTreeVO> buildPermissionTree(List<Permission> permissions) {
        List<PermissionTreeVO> treeVOs = new ArrayList<>();
        if (CollectionUtil.isEmpty(permissions)) {
            return treeVOs;
        }

        // Convert to VO
        List<PermissionTreeVO> allNodes = permissions.stream()
                .map(p -> {
                    PermissionTreeVO vo = new PermissionTreeVO();
                    BeanUtil.copyProperties(p, vo);
                    vo.setName(p.getPermissionName());
                    return vo;
                })
                .collect(Collectors.toList());

        // Build tree
        // Find roots (parentId == 0 or null)
        List<PermissionTreeVO> roots = allNodes.stream()
                .filter(node -> node.getParentId() == null || node.getParentId() == 0)
                .collect(Collectors.toList());

        for (PermissionTreeVO root : roots) {
            root.setChildren(getChildren(root.getId(), allNodes));
            treeVOs.add(root);
        }

        return treeVOs;
    }

    private List<PermissionTreeVO> getChildren(Long parentId, List<PermissionTreeVO> allNodes) {
        List<PermissionTreeVO> children = allNodes.stream()
                .filter(node -> Objects.equals(node.getParentId(), parentId))
                .collect(Collectors.toList());

        for (PermissionTreeVO child : children) {
            child.setChildren(getChildren(child.getId(), allNodes));
        }
        return children;
    }
}
