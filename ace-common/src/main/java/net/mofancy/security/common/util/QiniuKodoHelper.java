package net.mofancy.security.common.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import net.mofancy.security.common.properties.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

/**
 * 阿里云oss文件上传助手
 * @author zwq
 * @version 1.0
 * @date 2019/12/9 0009 下午 2:05
 */
@Component
@Slf4j
public class QiniuKodoHelper {

    private static String ENDPOINT = ApplicationProperties.QN_ENDPOINT;

    private static String ACCESSKEYID = ApplicationProperties.QN_ACCESSKEYID;

    private static String ACCESSKEYSECRET = ApplicationProperties.QN_ACCESSKEYSECRET;

    private static String BUCKET = ApplicationProperties.QN_BUCKET;

    /**
     * 上传图片
     * @author zwq
     * @date 2019/12/9 0009
     * @param bytes 文件字节数组
     * @param extension 文件扩展名
     * @return java.lang.String
     */
    public static String uploadPic(byte[] bytes, String extension) {
        return uploadFile(bytes, extension, "pic","");
    }

    /**
     * 上传文档
     * @author zwq
     * @date 2019/12/9 0009
     * @param bytes 文件字节数组
     * @param extension 文件扩展名
     * @return java.lang.String
     */
    public static String uploadDoc(byte[] bytes, String extension) {
        return uploadFile(bytes, extension, "doc","");
    }

    /**
     * 上传视频
     * @author zwq
     * @date 2019/12/9 0009
     * @param bytes 文件字节数组
     * @param extension 文件扩展名
     * @return java.lang.String
     */
    public static String uploadVideo(byte[] bytes, String extension) {
        return uploadFile(bytes, extension, "video","");
    }

    /**
     * 上传音频
     * @author zwq
     * @date 2019/12/9 0009
     * @param bytes 文件字节数组
     * @param extension 文件扩展名
     * @return java.lang.String
     */
    public static String uploadAudio(byte[] bytes, String extension) {
        return uploadFile(bytes, extension, "audio","");
    }

    /**
     * 普通上传文件
     * @author zwq
     * @date 2019/12/9 0009
     * @param bytes 文件字节数组
     * @param extension 文件扩展名
     * @param catalog 文件存放目录
     * @param objectName 文件名
     * @return java.lang.String
     */
    public static String uploadFile(byte[] bytes,String extension,String catalog,String objectName) {

        if ((bytes == null) || (bytes.length == 0)) {
            log.info("上传数据为空,取消本次上传");
            return "";
        }

        if(StringUtils.isEmpty(objectName)) {
            objectName = UUID.randomUUID().toString().replaceAll("-", "");
        }


        //文件名称
        String fileName = catalog+"/"+DateFormatUtils.format(new Date(), "yyyyMM")+"/"+objectName+"."+extension;


        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释
        //...生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        Auth auth = Auth.create(ACCESSKEYID, ACCESSKEYSECRET);
        String upToken = auth.uploadToken(BUCKET);
        System.out.println(upToken);
        String localTempDir = Paths.get(System.getenv("java.io.tmpdir"), BUCKET).toString();
        try {
            //设置断点续传文件进度保存目录
            FileRecorder fileRecorder = new FileRecorder(localTempDir);
            UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
            try {
                Response response = uploadManager.put(bytes, fileName, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String picUrl = "http://"+ENDPOINT+"/"+fileName;
        return picUrl;
    }


}
