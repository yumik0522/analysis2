package net.mofancy.security.auth;

import net.mofancy.security.api.vo.user.UserInfo;
import net.mofancy.security.auth.common.util.jwt.JWTInfo;
import net.mofancy.security.auth.util.user.JwtTokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zwq
 * @version 1.0
 * @date 2019/11/20 0020 下午 5:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={AuthBootstrap.class})
public class AuthBootstrapTest {

    private static final Logger log = LoggerFactory.getLogger(AuthBootstrapTest.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void generatorToken() {
        UserInfo info = new UserInfo ();
        info.setId("1");
        try {
            String token = jwtTokenUtil.generateAppToken(new JWTInfo(info.getUsername(), info.getId() + "", info.getName()));
            log.info(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
