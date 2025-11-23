package com.alicloud.auth.bean.VO;

import lombok.Data;

import java.util.List;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
@Data
public class PermissionTreeVO {

    private Long id;
    private Long parentId;
    private String name;
    private String path;
    private String component;
    private String icon;
    private String permissionCode;
    private Integer type;
    private List<PermissionTreeVO> children;

}
