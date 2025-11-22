package com.alicloud.common.exception;

/**
 * 账户锁定异常
 * @author Claude
 * @date 2025-11-18
 */
public class AccountLockedException extends RuntimeException {

    public AccountLockedException(String message) {
        super(message);
    }

    public AccountLockedException(String message, Throwable cause) {
        super(message, cause);
    }
}