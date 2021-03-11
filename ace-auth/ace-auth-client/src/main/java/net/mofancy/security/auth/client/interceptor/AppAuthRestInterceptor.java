package net.mofancy.security.auth.client.interceptor;

import net.mofancy.security.auth.client.annotation.IgnoreAppToken;
import net.mofancy.security.auth.client.config.AppAuthConfig;
import net.mofancy.security.auth.client.jwt.AppAuthUtil;
import net.mofancy.security.auth.common.util.jwt.IJWTInfo;
import net.mofancy.security.common.context.BaseContextHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ace on 2017/9/10.
 */
public class AppAuthRestInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(AppAuthRestInterceptor.class);

    @Autowired
    private AppAuthUtil appAuthUtil;

    @Autowired
    private AppAuthConfig appAuthConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 配置该注解，说明不进行APP用户拦截
        IgnoreAppToken annotation = handlerMethod.getBeanType().getAnnotation(IgnoreAppToken.class);
        if (annotation == null) {
            annotation = handlerMethod.getMethodAnnotation(IgnoreAppToken.class);
        }

        String token = request.getHeader(appAuthConfig.getTokenHeader());
        if (StringUtils.isEmpty(token)) {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(appAuthConfig.getTokenHeader())) {
                        token = cookie.getValue()+"";
                    }
                }
            }
        }

        if (annotation != null && StringUtils.isEmpty(token)) {
            return super.preHandle(request, response, handler);
        }

        IJWTInfo infoFromToken = appAuthUtil.getInfoFromToken(token);
        BaseContextHandler.setMemberId(infoFromToken.getId());
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContextHandler.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
