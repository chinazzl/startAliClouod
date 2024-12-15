package com.alicloud.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: zhaolin
 * @Date: 2024/12/8
 **/
@Component
public class AutoFillDataComponent implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Date now = new Date();
        metaObject.setValue("createTime", now);
        metaObject.setValue("updateTime", now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Date now = new Date();
        this.strictUpdateFill(metaObject,"updateTime",Date.class,now);
    }
}
