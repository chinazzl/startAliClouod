package com.alicloud.utils;

import com.sun.istack.internal.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 22:20
 * @Description:
 */
@Component
public class RedisUtils<T> {

    @Resource
    private RedisTemplate<String,T> myRedisTemplate;

    public List<T> range(String key) {
        return myRedisTemplate.opsForList().range(key,0,-1);
    }

    public Boolean leftPush(String key, @NotNull List<T> ts) {
        boolean result = true;
        try {
            ts.forEach(t -> myRedisTemplate.opsForList().leftPush(key, t));
        }catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public Boolean rightPush(String key, @NotNull List<T> ts) {
        boolean result = true;
        try {
            ts.forEach(t -> myRedisTemplate.opsForList().rightPush(key, t));
        }catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public Boolean existsKey(@NotNull String key) {
        return myRedisTemplate.hasKey(key);
    }
}
