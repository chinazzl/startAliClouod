package com.alicloud.common.config.security;

import com.alicloud.common.filter.JwtAuthenticationTokenFilter;
import com.alicloud.common.handler.DeniedAccessExceptionHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @author: zhaolin
 * @Date: 2024/12/8
 *EnableGlobalMethodSecurity: 允许在接口中增加认证
 **/
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Resource
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Resource
    AuthenticationEntryPoint authenticationEntryPoint;
    @Resource
    DeniedAccessExceptionHandler deniedAccessExceptionHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 获取AuthenticationMangerBean
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF保护
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagerConfig -> {
                    sessionManagerConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

                })
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/v1/users/login")
                            .anonymous()
                            .requestMatchers("/v1/public/**")
                            .permitAll()
                            .anyRequest()
                            .authenticated();
                });
//               .and()
//                .cors();

        // 增加鉴权异常处理器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint)
                            .accessDeniedHandler(deniedAccessExceptionHandler);
                });
//                .exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPoint)
//                .accessDeniedHandler(deniedAccessExceptionHandler);
        return http.build();
    }

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                // 禁用CSRF保护
//                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                // login接口可以匿名访问，直接放行
//                .antMatchers("/v1/users/login").anonymous()
//                // 其余的请求都需要验证
//                .anyRequest().authenticated();
////                .and()
////                .cors();
//        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
//                // 增加鉴权异常处理器
//                .exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPoint)
//                .accessDeniedHandler(deniedAccessExceptionHandler);
//    }
}
