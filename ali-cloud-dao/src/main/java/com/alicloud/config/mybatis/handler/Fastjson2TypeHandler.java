package com.alicloud.config.mybatis.handler;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson2.JSON;
import com.alicloud.constant.JSONFeature;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * @author: zhaolin
 * @Date: 2024/12/8
 **/
@Slf4j
@MappedTypes({Object.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class Fastjson2TypeHandler extends AbstractJsonTypeHandler<Object> {
    private final Class<?> type;

    public Fastjson2TypeHandler(Class<?> type) {
        super(type);
        if (log.isTraceEnabled()) {
            log.trace("Fastjson2TypeHandler(" + type + ")");
        }
        Assert.notNull(type, "Type must not be null");
        this.type = type;
    }

    @Override
    public Object parse(String json) {
        return JSON.parseObject(json, type);
    }

    @Override
    public String toJson(Object obj) {
        return JSON.toJSONString(obj, JSONFeature.FASTJSON2_WRITE_FEATURES);
    }
}
