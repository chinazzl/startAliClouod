package com.alicloud.common.interceptor;

import brave.Tracer;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import feign.Util;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**********************************
 * @author zhang zhao lin
 * @date 2025年11月28日 15:37
 * @Description:
 **********************************/
@Slf4j
public class OkHttpInterceptor implements Interceptor {

    private Tracer tracer;

    public OkHttpInterceptor(Tracer tracer) {
        this.tracer = tracer;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        URI uri = chain.request().url().uri();
        String path = uri.getPath();
        String host = uri.getHost();
        StopWatch stopWatch = StopWatch.createStarted();
        Response response = null;
        int status = 999;
        try {
            response = chain.proceed(chain.request());
            status = response.code();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            long time = stopWatch.getTime(TimeUnit.MILLISECONDS);
            String timestr = DateUtil.formatBetween(time, BetweenFormatter.Level.MILLISECOND);
            stopWatch.stop();
            traceLog(chain.request(), response, status, timestr);
        }
        return null;
    }

    private void traceLog(Request request, Response response, int status, String timestr) {
        log.info("okHttp log: traceId[{}], parent-span-id [{}],span-id [{}], uri[{}], 耗时：[{}], method [{}], headers [{}], \r\n requestBody [{}] \r\n status [{}]\r\n responseBody [{}]",
                (Objects.isNull(tracer.currentSpan()) || Objects.isNull(tracer.currentSpan().context()) ? "" : tracer.currentSpan().context().traceIdString()),
                (Objects.isNull(tracer.currentSpan()) || Objects.isNull(tracer.currentSpan().context()) ? "" : tracer.currentSpan().context().parentIdString()),
                (Objects.isNull(tracer.currentSpan()) || Objects.isNull(tracer.currentSpan().context()) ? "" : tracer.currentSpan().context().spanIdString()),
                request.url().url(),
                timestr,
                request.method(),
                request.headers(),
                readRequestBody(request),
                status,
                readResponseBody(response)
        );
    }

    private String readRequestBody(Request request) {
        RequestBody requestBody = request.body();
        if (requestBody == null) {
            return "";
        }
        Buffer buffer = new Buffer();
        try {
            requestBody.writeTo(buffer);
            Charset charset = StandardCharsets.UTF_8;
            return buffer.readString(charset);
        } catch (IOException e) {
            log.error("读取requestBody失败：{}", e.getMessage(), e);
            return "";
        }
    }

    private String readResponseBody(Response response) {
        if (response == null || response.body() == null ||
                response.headers().get(HttpHeaders.CONTENT_TYPE) == null ||
                !(MediaType.APPLICATION_JSON_VALUE.equals(response.headers().get(HttpHeaders.CONTENT_TYPE))) ||
                MediaType.parseMediaType(Objects.requireNonNull(response.headers().get(HttpHeaders.CONTENT_TYPE))).equalsTypeAndSubtype(MediaType.APPLICATION_JSON_UTF8)) {
            return "";
        }
        return cloneResponseBody(response);
    }

    private String cloneResponseBody(Response response) {
        ResponseBody body = response.body();
        if (body == null) {
            return "";
        }
        try {
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();
            Buffer copyBuffer = buffer.clone();
            Charset charset = body.contentType() != null ? body.contentType().charset(StandardCharsets.UTF_8) : StandardCharsets.UTF_8;
            return copyBuffer.readString(charset);
        } catch (Exception e) {
            log.error("克隆response body失败。{}", e.getMessage(), e);
            return "";
        }

    }
}
