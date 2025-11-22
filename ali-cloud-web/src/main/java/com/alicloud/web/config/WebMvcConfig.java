package com.alicloud.web.config;

import com.alicloud.web.filter.LoginSecurityFilter;
import com.alicloud.web.interceptor.LoginSecurityInterceptor;
import jakarta.annotation.Resource;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**********************************
 * @author zhang zhao lin
 * @date 2022年10月05日 14:03
 * @Description:
 **********************************/
@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private LoginSecurityInterceptor loginSecurityInterceptor;

    /**
     * 注册登录安全过滤器
     */
    @Bean
    public FilterRegistrationBean<LoginSecurityFilter> loginSecurityFilterRegistration(LoginSecurityFilter loginSecurityFilter) {
        FilterRegistrationBean<LoginSecurityFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(loginSecurityFilter);
        registration.addUrlPatterns("/*");
        registration.setName("loginSecurityFilter");
        registration.setOrder(1); // 设置过滤器顺序
        return registration;
    }

    /**
     * 注册登录安全拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginSecurityInterceptor)
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns(
                        "/static/**",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                );
    }

}
