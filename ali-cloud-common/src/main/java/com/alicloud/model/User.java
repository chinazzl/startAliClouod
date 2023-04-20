package com.alicloud.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 15:49
 * @Description:
 */
@Data
@Getter
@Setter
@EqualsAndHashCode
public class User {

    private String id;

    private String userName;

    private String passWord;

    private String email;

    private int loginType;

    private String mobile;

}
