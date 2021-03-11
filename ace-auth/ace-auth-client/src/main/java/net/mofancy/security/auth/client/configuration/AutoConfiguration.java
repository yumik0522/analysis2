package net.mofancy.security.auth.client.configuration;

import net.mofancy.security.auth.client.config.AppAuthConfig;
import net.mofancy.security.auth.client.config.ServiceAuthConfig;
import net.mofancy.security.auth.client.config.UserAuthConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ace on 2017/9/15.
 */
@Configuration
@ComponentScan({"net.mofancy.security.auth.client","net.mofancy.security.auth.common.event"})
public class AutoConfiguration {
    @Bean
    ServiceAuthConfig getServiceAuthConfig(){
        return new ServiceAuthConfig();
    }

    @Bean
    UserAuthConfig getUserAuthConfig(){
        return new UserAuthConfig();
    }

    @Bean
    AppAuthConfig getAppAuthConfig(){
        return new AppAuthConfig();
    }

}
