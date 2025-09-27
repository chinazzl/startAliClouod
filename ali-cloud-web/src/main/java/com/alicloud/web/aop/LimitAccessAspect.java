package com.alicloud.web.aop;

import com.alicloud.annotation.LimitAccess;
import com.alicloud.enums.LimitType;
import com.alicloud.utils.IpUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**********************************
 * @author zhang zhao lin
 * @date 2022年10月02日 0:31
 * @Description: 限流切面
 **********************************/
@Aspect
@Component
public class LimitAccessAspect {

    private Logger logger = LoggerFactory.getLogger(LimitAccessAspect.class);

    @Resource
    private RedisTemplate<Object, Object> myRedisTemplate;
    @Autowired
    private RedisScript<Long> redisScript;

    @Before("@annotation(limitAccess)")
    public void doBefore(JoinPoint joinPoint, LimitAccess limitAccess) {
        String key = limitAccess.key();
        int time = limitAccess.time();
        int count = limitAccess.count();
        String combinKey = combinKey(limitAccess, joinPoint);
        List<Object> keys = Collections.singletonList(combinKey);
        try {
            /*
                lua表达式：先从redis中获取指定key的 value，如果value 大于设置的count，
                则返回说明超过限流，如果没有超过，则+1,并返回+1后的结果
             */
            Long number = myRedisTemplate.execute(redisScript, keys, count, time);
            if (number != null && number.intValue() > count) {
                throw new RuntimeException("访问过于频繁，请稍后再试。");
            }
            logger.info("限制请求'{}'，当前请求'{}'，缓存key'{}'", count, number.intValue(), key);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("服务器限流异常，请稍后再试。");
        }
    }

    /**
     * 根据接口限流类型进行设置redis key。
     *
     * @param limitAccess
     * @param joinPoint
     * @return
     */
    public String combinKey(LimitAccess limitAccess, JoinPoint joinPoint) {
        StringBuilder buffer = new StringBuilder(limitAccess.key());
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        if (limitAccess.limitType() == LimitType.IP) {
            buffer.append(IpUtils.getIpAddress(request)).append("-");
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        buffer.append(targetClass.getName()).append("-")
                .append(method.getName());
        return buffer.toString();
    }
}
