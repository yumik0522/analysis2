package net.mofancy.security.admin;

import com.ace.cache.EnableAceCache;
import com.spring4all.swagger.EnableSwagger2Doc;
import net.mofancy.security.admin.jqueue.Jqueue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-05-25 12:44
 */
@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
//@EnableFeignClients({"net.mofancy.security.auth.client.feign"})
@EnableScheduling
//@EnableAceAuthClient
@EnableAceCache
@EnableTransactionManagement
@MapperScan("net.mofancy.security.admin.mapper")
@EnableSwagger2Doc
public class AdminBootstrap {

    private static String[] args;
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        AdminBootstrap.args = args;
        context = SpringApplication.run(AdminBootstrap.class, args);
        try {
            new Jqueue(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void restart() {
        context.close();
        AdminBootstrap.context = SpringApplication.run(AdminBootstrap.class, args);
    }
}
