package com.alicloud.common.config.feign;

import brave.Tracer;
import com.alicloud.common.interceptor.OkHttpInterceptor;
import com.alicloud.common.utils.SSLSocketClientUtils;
import feign.Contract;
import feign.Logger;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.X509TrustManager;
import java.util.concurrent.TimeUnit;

/**********************************
 * @author zhang zhao lin
 * @date 2025年11月28日 15:07
 * @Description:
 **********************************/
@Configuration
@ConditionalOnClass(FeignClient.class)
@AutoConfigureBefore(FeignClientsConfiguration.class)
public class OpenFeignOkHttpConfiguration {

    private final Tracer tracer;

    public OpenFeignOkHttpConfiguration(Tracer tracer) {
        this.tracer = tracer;
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Contract  feignContract() {
        return new feign.Contract.Default();
    }

    @Bean
    public okhttp3.OkHttpClient okhttpClient() {
        X509TrustManager x509TrustManager = SSLSocketClientUtils.getX509TrustManager();
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(280,TimeUnit.SECONDS)
                .writeTimeout(120,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .sslSocketFactory(SSLSocketClientUtils.getSSLSocketFactory(x509TrustManager),x509TrustManager)
                .hostnameVerifier(SSLSocketClientUtils.getHostnameVerifier())
                .connectionPool(new ConnectionPool())
                .addInterceptor(new OkHttpInterceptor(tracer))
                .build();

    }
}
