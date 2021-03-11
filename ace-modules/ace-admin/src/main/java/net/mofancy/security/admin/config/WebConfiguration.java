package net.mofancy.security.admin.config;

import net.mofancy.security.common.handler.WgExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author ace
 * @date 2017/9/8
 */
@Configuration("admimWebConfig")
@Primary
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    WgExceptionHandler getWgExceptionHandler() {
        return new WgExceptionHandler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(getServiceAuthRestInterceptor()).
//                addPathPatterns(getIncludePathPatterns()).addPathPatterns("/api/user/validate");
//        registry.addInterceptor(getUserAuthRestInterceptor()).
//                addPathPatterns(getIncludePathPatterns());
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/vue/**").addResourceLocations("classpath:vue/");
    }

//    @Bean
//    ServiceAuthRestInterceptor getServiceAuthRestInterceptor() {
//        return new ServiceAuthRestInterceptor();
//    }
//
//    @Bean
//    UserAuthRestInterceptor getUserAuthRestInterceptor() {
//        return new UserAuthRestInterceptor();
//    }

//    @Bean
//    public MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        factory.setLocation("/weigrass/admin/tmp");//指定临时文件路径，这个路径可以随便写
//        return factory.createMultipartConfig();
//    }

    /**
     * 需要用户和服务认证判断的路径
     * @return
     */
    private ArrayList<String> getIncludePathPatterns() {
        ArrayList<String> list = new ArrayList<>();
        String[] urls = {
                "/element/**",
                "/gateLog/**",
                "/group/**",
                "/groupType/**",
                "/menu/**",
                "/user/**"
        };
        Collections.addAll(list, urls);
        return list;
    }

}
