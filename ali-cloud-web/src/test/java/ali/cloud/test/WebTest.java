package ali.cloud.test;

import com.alicloud.constant.RedisConstant;
import com.alicloud.model.User;
import com.alicloud.utils.RedisUtils;
import com.alicloud.web.ALCloudWeb9000;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/26 14:09
 * @Description:
 */
@SpringBootTest(classes = ALCloudWeb9000.class)
@ExtendWith(SpringExtension.class)
public class WebTest {

    @Autowired
    private RedisUtils<User> redisUtils;

    @Test
    public void redisTest() {
        List<User> ls  = new ArrayList<>();
        User user = new User();
        user.setEmail("ssss");
        ls.add(user);
        redisUtils.leftPush(RedisConstant.REDIS_KEY_USER,ls);
    }
}
