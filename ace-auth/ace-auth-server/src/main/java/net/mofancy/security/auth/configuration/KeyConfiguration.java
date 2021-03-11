package net.mofancy.security.auth.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author ace
 * @create 2017/12/17.
 */
@Configuration
@Data
public class KeyConfiguration {
    @Value("${jwt.admin.rsa-secret}")
    private String userSecret;
    @Value("${jwt.app.rsa-secret}")
    private String appSecret;
    @Value("${client.rsa-secret}")
    private String serviceSecret;
    private byte[] userPubKey;
    private byte[] userPriKey;
    private byte[] servicePriKey;
    private byte[] servicePubKey;
    private byte [] appPriKey;
    private byte [] appPubKey;
}
