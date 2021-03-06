package net.mofancy.security.auth.feign;

import net.mofancy.security.api.vo.user.UserInfo;
import net.mofancy.security.auth.configuration.FeignConfiguration;
import net.mofancy.security.auth.util.user.JwtAuthenticationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-21 8:11
 */
@FeignClient(value = "ace-admin",configuration = FeignConfiguration.class)
public interface IUserService {
  @RequestMapping(value = "/api/user/validate", method = RequestMethod.POST)
  UserInfo validate(@RequestBody JwtAuthenticationRequest authenticationRequest);
}
