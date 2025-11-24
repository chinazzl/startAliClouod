package com.alicloud.dao.enums;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
public enum MenuType implements BaseEnum{
    DIRECTORY(1, "目录"),
    MENU(2, "菜单"),
    BUTTON(3, "按钮"),
    API(4, "接口");

    private final int code;
    private final String message;

    MenuType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return 0;
    }
}
