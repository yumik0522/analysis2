package net.mofancy.security.common.properties;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Slf4j
public class ApplicationProperties {

    //------------阿里云OSS配置------------------
    //终端
    public static String ENDPOINT;
    //阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    public static String ACCESSKEYID;
    //密钥
    public static String ACCESSKEYSECRET;
    //命名空间
    public static String BUCKET;


    //------------七牛云KODO配置------------------
    //终端
    public static String QN_ENDPOINT;

    public static String QN_ACCESSKEYID;
    //密钥
    public static String QN_ACCESSKEYSECRET;
    //命名空间
    public static String QN_BUCKET;


    static {
        Properties properties = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
        	log.error("application.properties配置文件读取出错", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            	log.error("application.properties配置文件读取出错", e);
            }
        }

        ENDPOINT = properties.getProperty("aliyun.oss.endpoint");
        ACCESSKEYID = properties.getProperty("aliyun.oss.accessKeyId");
        ACCESSKEYSECRET = properties.getProperty("aliyun.oss.accessKeySecret");
        BUCKET = properties.getProperty("aliyun.oss.bucket");


        QN_ENDPOINT = properties.getProperty("qiniu.kodo.endpoint");
        QN_ACCESSKEYID = properties.getProperty("qiniu.kodo.accessKeyId");
        QN_ACCESSKEYSECRET = properties.getProperty("qiniu.kodo.accessKeySecret");
        QN_BUCKET = properties.getProperty("qiniu.kodo.bucket");

    }

}
