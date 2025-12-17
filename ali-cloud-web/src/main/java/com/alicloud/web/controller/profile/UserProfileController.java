package com.alicloud.web.controller.profile;

import com.alicloud.common.model.CommonResponse;
import com.alicloud.common.model.profile.HealthProfilesVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhaolin
 * @Date: 2025/12/17
 * @Description:
 **/
@RestController
@RequestMapping("/api/health/profiles")
public class UserProfileController {


    @PostMapping("/createProfile")
    public CommonResponse<HealthProfilesVO>  createProfile(@RequestBody HealthProfilesVO healthProfilesVO) {

    }

}
