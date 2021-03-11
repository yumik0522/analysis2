package net.mofancy.security.auth.client.jwt;

import net.mofancy.security.auth.client.config.AppAuthConfig;
import net.mofancy.security.auth.common.util.jwt.IJWTInfo;
import net.mofancy.security.auth.common.util.jwt.JWTHelper;
import net.mofancy.security.common.exception.common.AppTokenIllegalException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ace on 2017/9/15.
 */
@Configuration
public class AppAuthUtil {
    @Autowired
    private AppAuthConfig appAuthConfig;
    public IJWTInfo getInfoFromToken(String token) throws Exception {
        try {
            return JWTHelper.getInfoFromToken(token, appAuthConfig.getPubKeyByte());
        }catch (ExpiredJwtException ex){
            throw new AppTokenIllegalException("User token expired!");
        }catch (SignatureException ex){
            throw new AppTokenIllegalException("User token signature error!");
        }catch (IllegalArgumentException ex){
            throw new AppTokenIllegalException("User token is null or empty!");
        }
    }
}
