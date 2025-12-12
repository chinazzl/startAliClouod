package com.alicloud.common.handler;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.UUID;
import com.alicloud.common.utils.RedisUtils;
import com.alicloud.common.utils.jwt.JWTInfo;
import com.alicloud.common.utils.jwt.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
@Slf4j
@Component
public class TokenManager {

    @Resource
    JwtTokenUtil jwtTokenUtil;

    @Resource
    private RedisUtils<String> redisUtils;


    @Value("${jwt.access-token.expire:30}")
    private int accessTokenExpireMinutes;

    @Value("${jwt.refresh-token.expire:1440}") // 24小时
    private int refreshTokenExpireMinutes;

    @Value("${jwt.blacklist-prefix:token:blacklist:}")
    private String blacklistPrefix;

    @Value("${jwt.refresh-token.prefix:token:refresh:}")
    private String refreshTokenPrefix;

    @Value("${jwt.refresh-token.prefix:token:user:}")
    private String userTokenPrefix;


    public TokenPair generateTokenPair(Long userId, String username, String password) {
        try {
            JWTInfo jwtInfo = new JWTInfo();
            jwtInfo.setId(String.valueOf(userId));
            jwtInfo.setUsername(username);
            jwtInfo.setPassword(password);
            String accessToken = jwtTokenUtil.generateToken(jwtInfo);
            String refreshToken = generateRefreshToken(userId, username);
            // 缓存用户token
            String userTokenKey = userTokenPrefix + userId;
            redisUtils.set(userTokenKey, accessToken, accessTokenExpireMinutes, TimeUnit.MINUTES);
            return new TokenPair(accessToken, refreshToken);
        } catch (Exception e) {
            log.error("生成Token失败", e);
            throw new RuntimeException("生成Token失败", e);
        }
    }

    /**
     * 生成访问token
     */
    public String generateAccessToken(Long userId, String username, String password) {
        JWTInfo jwtInfo = new JWTInfo();
        jwtInfo.setUsername(username);
        jwtInfo.setPassword(password);
        jwtInfo.setId(String.valueOf(userId));
        try {
            return jwtTokenUtil.generateToken(jwtInfo);
        } catch (Exception e) {
            log.error("生成accessToken失败", e);
            throw new RuntimeException("生成accessTken失败", e);
        }
    }

    /**
     * 生成refresh访问token
     */
    public String generateRefreshToken(Long userId, String username) {
        String refreshToken = UUID.randomUUID().toString().replace("-", "");

        // 存储刷新Token映射
        String key = refreshTokenPrefix + refreshToken;
        String value = userId + ":" + username;
        redisUtils.set(key, value, accessTokenExpireMinutes, TimeUnit.MINUTES);
        return refreshToken;
    }

    /**
     * 解析Token
     */
    public JWTInfo parseToken(String token) {
        try {
            return jwtTokenUtil.getInfoFromToken(token);
        } catch (ExpiredJwtException e) {
            log.error("Token已过期", e);
            throw new RuntimeException("Token已过期", e);
        } catch (SignatureException e) {
            log.error("Token签名验证失败", e);
            throw new RuntimeException("Token签名验证失败", e);
        } catch (Exception e) {
            log.error("Token 解析失败", e);
            throw new RuntimeException("Token解析失败", e);
        }
    }

    /**
     * 把token放入黑名单
     */
    public void blacklistToken(String token) {
        try {
            JWTInfo jwtInfo = parseToken(token);
            long ttl = ((long) jwtTokenUtil.getExpire() * 60 * 1000) - (long) accessTokenExpireMinutes * 60 * 1000;
            if (ttl > 0) {
                String key = blacklistPrefix + token;
                redisUtils.set(key, "1", ttl, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            log.warn("加入Token黑名单失败,token => {}", token, e);
        }
    }

    /**
     * 检验token是否存在黑名单
     */
    public boolean isTokenBlacklisted(String token) {
        String key = blacklistPrefix + token;
        return redisUtils.existsKey(key);
    }

    public Pair<JWTInfo, Boolean> isTokenExpired(String token) {
        try {
            JWTInfo infoFromToken = jwtTokenUtil.getInfoFromToken(token);
            return Pair.of(infoFromToken, false);
        } catch (ExpiredJwtException e) {
            log.info("token expired,{}", token, e);
            return Pair.of(null, true);
        } catch (Exception e) {
            log.error("Token验证失败:{}", token, e);
            return Pair.of(null, true);
        }
    }

    /**
     * 刷新token
     */
    public String refreshToken(String token) {
        String key = refreshTokenPrefix + token;
        String value = redisUtils.get(key);
        if (value == null) {
            throw new RuntimeException("刷新Token无效或已过期");
        }
        String[] parts = value.split(":");
        Long userId = Long.valueOf(parts[0]);
        String username = parts[1];
        // 删除旧的刷新token
        redisUtils.delete(key);
        //生成新的token
        return generateAccessToken(userId, username, null);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenPair {
        private String accessToken;
        private String refreshToken;
    }
}
