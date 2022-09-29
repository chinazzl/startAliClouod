package com.alicloud;

import org.junit.Test;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.time.Duration;
import java.util.Properties;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/26 13:38
 * @Description:
 */
public class CommonTest {
    public static void main(String[] args) {
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(new ClassPathResource("redisconfig.yml"));
        Properties yamlProperties = yamlPropertiesFactoryBean.getObject();
        String s = yamlProperties.get("spring.redis.lettuce.pool.max-wait").toString();
        System.out.println(s);
        Duration parse = Duration.parse(s);
        System.out.println(parse);
    }


}
