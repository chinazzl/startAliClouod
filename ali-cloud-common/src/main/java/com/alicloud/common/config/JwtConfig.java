package com.alicloud.common.config;

import com.alicloud.common.utils.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**********************************
 * @author zhang zhao lin
 * @date 2025年11月24日 14:44
 * @Description:
 **********************************/
@Configuration
@RefreshScope
public class JwtConfig {

    @Value("${jwt.expire}")
    private int expire;

    @Value("${jwt.rsa-secret}")
    private String rsaSecret;

    @Bean("jwtTokenUtil")
    public JwtTokenUtil getJwtTokenUtil(){
        JwtTokenUtil util = new JwtTokenUtil();
        util.setExpire(expire);
        util.setUserSecret(rsaSecret);
        return util;
    }
}
