package com.alicloud.common.handler;

import com.alicloud.common.exception.AccountLockedException;
import com.alicloud.common.exception.UnauthorizedException;
import com.alicloud.common.exception.UserException;
import com.alicloud.common.model.BaseModelVo;
import com.alicloud.common.model.ModelVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @author Claude
 * @date 2025-11-18
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理账户锁定异常
     */
    @ExceptionHandler(AccountLockedException.class)
    public ModelVo handleAccountLockedException(AccountLockedException e) {
        log.warn("账户锁定异常: {}", e.getMessage());
        ModelVo modelVo = new ModelVo();
        modelVo.setCodeEnum(BaseModelVo.Code.ERROR, e.getMessage());
        return modelVo;
    }

    @ExceptionHandler(UserException.class)
    public ModelVo handleUserException(UserException e) {
        log.error("用户异常：{}",e.getMessage(),e);
        ModelVo modelVo = new ModelVo();
        modelVo.setCodeEnum(BaseModelVo.Code.ERROR, "用户存在异常，请稍后重试");
        return modelVo;
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ModelVo handleAuthenticationException(UnauthorizedException e) {
        log.error("鉴权异常：{}",e.getMessage(),e);
        ModelVo modelVo = new ModelVo();
        modelVo.setCodeEnum(BaseModelVo.Code.ERROR, e.getMessage() + "，请稍后重试");
        return modelVo;
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ModelVo handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        ModelVo modelVo = new ModelVo();
        modelVo.setCodeEnum(BaseModelVo.Code.ERROR, "系统异常，请稍后重试");
        return modelVo;
    }
}