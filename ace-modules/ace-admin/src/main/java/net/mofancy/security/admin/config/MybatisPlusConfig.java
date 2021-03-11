package net.mofancy.security.admin.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zwq
 * @version 1.0
 * @date 2020/1/22 0022 下午 6:49
 */
//Spring boot方式
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {


}