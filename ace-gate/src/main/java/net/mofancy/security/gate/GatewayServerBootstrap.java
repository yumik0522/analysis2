package net.mofancy.security.gate;

import net.mofancy.security.auth.client.EnableAceAuthClient;
import net.mofancy.security.gate.utils.DBLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ace
 * @create 2018/3/12.
 */
@EnableDiscoveryClient
@EnableAceAuthClient
@EnableFeignClients({"net.mofancy.security.auth.client.feign","net.mofancy.security.gate.feign"})
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class GatewayServerBootstrap {
    public static void main(String[] args) {
        DBLog.getInstance().start();
        SpringApplication.run(GatewayServerBootstrap.class, args);
    }
}
