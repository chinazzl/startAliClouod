package ali.cloud.test;

import com.alicloud.bean.Menu;
import com.alicloud.mapper.UserMapper;
import com.alicloud.model.UserVo;
import com.alicloud.utils.jwt.JWTInfo;
import com.alicloud.utils.jwt.JwtTokenUtil;
import com.alicloud.web.AliCloudWeb;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: zhaolin
 * @Date: 2024/12/8
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AliCloudWeb.class)
@Slf4j
public class AppTest {

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    JwtTokenUtil jwtTokenUtil;
    @Resource
    UserMapper userMapper;

    @Test
    public void testEncoderPassword() {
        String password = "1234";
        log.info("code1 {}",passwordEncoder.encode(password));
        log.info("code2 {}",passwordEncoder.encode(password));
    }

    @Test
    public void testJwt() throws Exception {
        String password = "1234";
        UserVo userVo = new UserVo();
        userVo.setPassword(password);
        System.out.println(jwtTokenUtil.generateToken(JWTInfo.of(userVo)));
        JWTInfo infoFromToken = jwtTokenUtil.getInfoFromToken("eyJhbGciOiJSUzI1NiJ9.eyJpZCI6IiIsInBhc3N3b3JkIjoiMTIzNCIsInVzZXJuYW1lIjoiIiwiZXhwIjoxNzMzOTIyMzQwfQ.DiKko1A2ARK_Abb1xhlvnB7eBOCqg9iGS9lt6KtaAPF3Y7zGe7SnSI_ryV1ASrJLrddqxbUEHjlPFau-jk3MH3dvR-Hg8ylZfZny1J3h_R8nu8zcTKoPUDy4qvdZenftmUziGQLAb9BhyWtRyE8Ened7C__hsfTeqcOQA8xpCXE");
        System.out.println(infoFromToken.toJsonObj());
        System.out.println(infoFromToken.toJsonObj());
    }

    @Test
    public void testUserMapper() {
        Long userId = 1L;
        List<Menu> userPermissions = userMapper.getUserPermission(userId);
        log.info(userPermissions.toString());
    }
}
