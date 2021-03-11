package net.mofancy.security.auth.service.impl;

import net.mofancy.security.api.vo.user.UserInfo;
import net.mofancy.security.auth.common.util.jwt.JWTInfo;
import net.mofancy.security.auth.feign.IUserService;
import net.mofancy.security.auth.service.AuthService;
import net.mofancy.security.auth.util.user.JwtAuthenticationRequest;
import net.mofancy.security.auth.util.user.JwtTokenUtil;
import net.mofancy.security.common.exception.auth.UserInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private IUserService userService;


    @Override
    public String login(JwtAuthenticationRequest authenticationRequest) throws Exception {
        UserInfo info = userService.validate(authenticationRequest);
        if (!StringUtils.isEmpty(info.getId())) {
            return jwtTokenUtil.generateToken(new JWTInfo(info.getUsername(), info.getId() + "", info.getName()));
        }
        throw new UserInvalidException("用户不存在或账户密码错误!");
    }

    @Override
    public void validate(String token) throws Exception {
        jwtTokenUtil.getInfoFromToken(token);
    }


    @Override
    public String refresh(String oldToken) throws Exception {
        return jwtTokenUtil.generateToken(jwtTokenUtil.getInfoFromToken(oldToken));
    }


}
