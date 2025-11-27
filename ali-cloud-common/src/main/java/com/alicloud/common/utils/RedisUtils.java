package com.alicloud.common.utils;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 22:20
 * @Description:
 */
@Component
@Slf4j
public class RedisUtils<T> {

    @Resource
    private RedisTemplate<String, T> myRedisTemplate;

    public List<T> range(String key) {
        return myRedisTemplate.opsForList().range(key, 0, -1);
    }

    public boolean set(String key, T value, long time, TimeUnit timeUnit) {
        boolean result = true;
        try {
            if (time <= 0) {
                myRedisTemplate.opsForValue().set(key, value);
            }else {
                myRedisTemplate.opsForValue().set(key, value, time, timeUnit);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            result = false;
        }
        return result;
    }

    public T get(String key) {
        return myRedisTemplate.opsForValue().get(key);
    }

    public Object hget(String key, String field) {
        return myRedisTemplate.opsForHash().get(key, field);
    }

    public void hset(String field, String key, Object value) {
        myRedisTemplate.opsForHash().put(field,key, value);
    }

    public boolean delete(String key) {
        return myRedisTemplate.delete(key);
    }

    public Boolean leftPush(String key, @NotNull List<T> ts, Long expireTime) {
        Boolean result = Boolean.TRUE;
        try {
            ts.forEach(t -> myRedisTemplate.opsForList().leftPush(key, t));
        } catch (Exception e) {
            e.printStackTrace();
            result = Boolean.FALSE;
        }
        return result;
    }

    public Boolean rightPush(String key, @NotNull List<T> ts) {
        Boolean result = Boolean.TRUE;
        try {
            ts.forEach(t -> myRedisTemplate.opsForList().rightPush(key, t));
        } catch (Exception e) {
            e.printStackTrace();
            result = Boolean.FALSE;
        }
        return result;
    }

    public Boolean existsKey(@NotNull String key) {
        return myRedisTemplate.hasKey(key);
    }

    public T opsValueForString(@NotNull String redisKey, T data) {
        try {
            Optional.of(data).ifPresent(d -> myRedisTemplate.opsForValue().set(redisKey, data));
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
