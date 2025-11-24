package com.alicloud.web.config;

import com.alicloud.web.filter.LoginSecurityFilter;
import com.alicloud.web.interceptor.LoginSecurityInterceptor;
import jakarta.annotation.Resource;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

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
     * 配置消息转换器，支持text/json内容类型
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.valueOf("text/json"));
        supportedMediaTypes.add(MediaType.valueOf("text/plain;charset=utf-8"));
        supportedMediaTypes.add(MediaType.ALL);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        converters.add(0, converter);
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
