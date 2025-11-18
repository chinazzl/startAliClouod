package com.alicloud.web.controller;

import com.alicloud.api.bean.vo.ModelVo;
import com.alicloud.common.model.BaseModelVo;
import com.alicloud.common.model.LoginFailRecord;
import com.alicloud.service.LoginFailService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * 管理员控制器 - 账户管理
 * @author Claude
 * @date 2025-11-18
 */
@Slf4j
@RestController
@RequestMapping("/v1/admin")
public class AdminController {

    @Resource
    private LoginFailService loginFailService;

    /**
     * 解锁用户账户
     */
    @PostMapping("/unlock-account/{userName}")
    public ModelVo unlockAccount(@PathVariable String userName) {
        ModelVo modelVo = new ModelVo();
        try {
            loginFailService.unlockAccount(userName);
            modelVo.setCodeEnum(BaseModelVo.Code.SUCCESS, "账户解锁成功");
            log.info("管理员解锁账户: {}", userName);
        } catch (Exception e) {
            log.error("解锁账户失败: {}", userName, e);
            modelVo.setCodeEnum(BaseModelVo.Code.ERROR, "解锁账户失败: " + e.getMessage());
        }
        return modelVo;
    }

    /**
     * 查看用户登录失败记录
     */
    @GetMapping("/login-fail-record/{userName}")
    public ModelVo getLoginFailRecord(@PathVariable String userName) {
        ModelVo modelVo = new ModelVo();
        try {
            LoginFailRecord record = loginFailService.getLoginFailRecord(userName);
            if (record != null) {
                modelVo.setCodeEnum(BaseModelVo.Code.SUCCESS);
                modelVo.getResult().put("record", record);
            } else {
                modelVo.setCodeEnum(BaseModelVo.Code.ERROR, "未找到用户登录记录");
            }
        } catch (Exception e) {
            log.error("获取登录失败记录失败: {}", userName, e);
            modelVo.setCodeEnum(BaseModelVo.Code.ERROR, "获取记录失败: " + e.getMessage());
        }
        return modelVo;
    }

    /**
     * 检查账户是否被锁定
     */
    @GetMapping("/check-account-locked/{userName}")
    public ModelVo checkAccountLocked(@PathVariable String userName) {
        ModelVo modelVo = new ModelVo();
        try {
            boolean isLocked = loginFailService.isAccountLocked(userName);
            modelVo.setCodeEnum(BaseModelVo.Code.SUCCESS);
            modelVo.getResult().put("userName", userName);
            modelVo.getResult().put("isLocked", isLocked);
        } catch (Exception e) {
            log.error("检查账户锁定状态失败: {}", userName, e);
            modelVo.setCodeEnum(BaseModelVo.Code.ERROR, "检查失败: " + e.getMessage());
        }
        return modelVo;
    }
}