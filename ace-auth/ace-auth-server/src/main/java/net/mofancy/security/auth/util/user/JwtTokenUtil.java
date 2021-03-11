package net.mofancy.security.auth.util.user;

import net.mofancy.security.auth.common.util.jwt.IJWTInfo;
import net.mofancy.security.auth.common.util.jwt.JWTHelper;
import net.mofancy.security.auth.configuration.KeyConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by ace on 2017/9/10.
 */
@Component
public class JwtTokenUtil {

    @Value("${jwt.admin.expire}")
    private int adminExpire;
    @Value("${jwt.app.expire}")
    private int appExpire;
    @Autowired
    private KeyConfiguration keyConfiguration;

    public String generateToken(IJWTInfo jwtInfo) throws Exception {
        return JWTHelper.generateToken(jwtInfo, keyConfiguration.getUserPriKey(),adminExpire);
    }

    public IJWTInfo getInfoFromToken(String token) throws Exception {
        return JWTHelper.getInfoFromToken(token, keyConfiguration.getUserPubKey());
    }

    public String generateAppToken(IJWTInfo jwtInfo) throws Exception {
        return JWTHelper.generateToken(jwtInfo, keyConfiguration.getAppPriKey(),appExpire);
    }

    public String generateAppRefreshToken(IJWTInfo jwtInfo) throws Exception {
        return JWTHelper.generateToken(jwtInfo, keyConfiguration.getAppPriKey(),appExpire+appExpire);
    }

    public IJWTInfo getInfoFromAppToken(String token) throws Exception {
        return JWTHelper.getInfoFromToken(token, keyConfiguration.getAppPubKey());
    }

    public Jws<Claims> parserAppToken(String token) throws Exception {
        return JWTHelper.parserToken(token, keyConfiguration.getAppPubKey());
    }


}
