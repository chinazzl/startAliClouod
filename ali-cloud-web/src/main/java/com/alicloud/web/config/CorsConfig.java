package com.alicloud.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**********************************
 * @author zhang zhao lin
 * @date 2022年10月05日 13:41
 * @Description:
 **********************************/
@Configuration
public class CorsConfig {

    private CorsConfiguration buildConfig() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许任何域名使用
//        config.addAllowedOrigin(("*"));
        config.addAllowedOriginPattern("*");
        // 允许任何头信息
        config.addAllowedHeader(("*"));
        // 允许在任何方法post、get
        config.addAllowedMethod(("*"));
        // 允许携带cookies
        config.setAllowCredentials(true);
        return config;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

}
