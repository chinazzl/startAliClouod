package com.alicloud.config.mybatis.handler;

import com.alicloud.enums.BaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zhaolin
 * @Date: 2024/12/5
 **/
public class CommonEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {
    private static final Map<String,String> TABLE_METHOD_OF_ENUM_TYPES = new ConcurrentHashMap<String, String>();
    private static final ReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();
    private final Class<E> enumClassType;
    private final Invoker getInvoker;

    public CommonEnumTypeHandler(Class<E> enumClassType) {
        if (enumClassType == null) {
            throw new IllegalArgumentException("enumClassType can't be null");
        }
        this.enumClassType = enumClassType;
        String name = "code";
        MetaClass metaClass = MetaClass.forClass(enumClassType, REFLECTOR_FACTORY);
        if (!BaseEnum.class.isAssignableFrom(enumClassType)) {
            name = findEnumValueFieldName(this.enumClassType).orElseThrow(() -> new IllegalArgumentException(String.format("Could not find @EnumValue in Class %s.",this.enumClassType.getName())));
        }
        this.getInvoker = metaClass.getGetInvoker(name);
    }

    private static Optional<String> findEnumValueFieldName(Class<?> clazz) {
        if (clazz != null && clazz.isEnum()) {
            String className = clazz.getName();
            return Optional.ofNullable(CollectionUtils.computeIfAbsent(TABLE_METHOD_OF_ENUM_TYPES,className, k -> {
                Optional<Field> fieldOptional = findEnumValueAnnotationField(clazz);
                return fieldOptional.map(Field::getName).orElse(null);
            }));
        }
        return Optional.empty();
    }

    private static Optional<Field> findEnumValueAnnotationField(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(EnumValue.class)).findFirst();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setObject(i, parameter);
        }else {
            ps.setObject(i,this.getValue(parameter),jdbcType.TYPE_CODE);
        }
    }

    private Object getValue(Object object) {
        try {
            return this.getInvoker.invoke(object,new Object[0]);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw ExceptionUtils.mpe(e);
        }
    }

    @Override
    public E getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        Integer value = resultSet.getInt(columnName);
        return this.valueOf(value);
    }

    private E valueOf(Object value) {
        E[] enumConstants = this.enumClassType.getEnumConstants();
        return Arrays.stream(enumConstants).filter(e -> equalsValue(value,getValue(e))).findAny().orElse(null);
    }

    /**
     * 值比较
     * @param sourceValue 数据库字段值
     * @param targetValue 当前枚举属性值
     * @return 是否匹配
     */
    private boolean equalsValue(Object sourceValue, Object targetValue) {
        String sValue = StringUtils.toStringTrim(sourceValue);
        String tValue = StringUtils.toStringTrim(targetValue);
        if (sourceValue instanceof Number && targetValue instanceof Number
            && new BigDecimal(sValue).compareTo(new BigDecimal(tValue)) == 0) {
            return true;
        }
        return Objects.equals(sValue, tValue);
    }

    @Override
    public E getNullableResult(ResultSet resultSet, int i) throws SQLException {
        Object value = resultSet.getInt(i);
        return this.valueOf(value);
    }

    @Override
    public E getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int value = callableStatement.getInt(i);
        return this.valueOf(value);
    }

}
