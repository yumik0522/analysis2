package net.mofancy.security.common.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.model.UploadFileRequest;
import com.aliyun.oss.model.UploadFileResult;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import net.mofancy.security.common.properties.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
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
public class AliyunOssHelper {

    private static String ENDPOINT = ApplicationProperties.ENDPOINT;

    private static String ACCESSKEYID = ApplicationProperties.ACCESSKEYID;

    private static String ACCESSKEYSECRET = ApplicationProperties.ACCESSKEYSECRET;

    private static String BUCKET = ApplicationProperties.BUCKET;

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
        return multipartUpload(bytes, extension, "video","");
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

        String bucketName = BUCKET;

        //文件名称
        String fileName = catalog+"/"+DateFormatUtils.format(new Date(), "yyyyMM")+"/"+objectName+"."+extension;

        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);
            // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
            ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(bytes));
            ossClient.setObjectAcl(bucketName, fileName, CannedAccessControlList.PublicRead);
            // 关闭OSSClient。
            ossClient.shutdown();
        } catch (Exception e) {
            log.error("oss上传异常", e);
            fileName = "";
        }
        String picUrl = "https://"+BUCKET+"."+ENDPOINT+"/"+fileName;
        return picUrl;
    }

    /**
     * 功能描述
     * @author zwq
     * @date 2020/1/13 0013
     * @param
     * @return
     */
    public static String multipartUpload(byte[] bytes,String extension,String catalog,String objectName) {

        if ((bytes == null) || (bytes.length == 0)) {
            log.info("上传数据为空,取消本次上传");
            return "";
        }

        if(StringUtils.isEmpty(objectName)) {
            objectName = UUID.randomUUID().toString().replaceAll("-", "");
        }

        String bucketName = BUCKET;

        //文件名称
        String fileName = catalog+"/"+DateFormatUtils.format(new Date(), "yyyyMM")+"/"+objectName+"."+extension;

        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);

            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, fileName);
            // The local file to upload---it must exist.
            uploadFileRequest.setUploadFile(new String(bytes));
            // Sets the concurrent upload task number to 5.
            uploadFileRequest.setTaskNum(5);
            // Sets the part size to 1MB.
            uploadFileRequest.setPartSize(1024 * 1024 * 5);
            // Enables the checkpoint file. By default it's off.
            uploadFileRequest.setEnableCheckpoint(true);

            UploadFileResult uploadResult = ossClient.uploadFile(uploadFileRequest);

            CompleteMultipartUploadResult multipartUploadResult = uploadResult.getMultipartUploadResult();
            ossClient.setObjectAcl(bucketName, fileName, CannedAccessControlList.PublicRead);
            // 关闭OSSClient。
            ossClient.shutdown();
        } catch (Exception e) {
            log.error("oss上传异常", e);
        } catch (Throwable throwable) {
            log.error("oss上传异常", throwable);
        }
        String picUrl = "https://"+BUCKET+"."+ENDPOINT+"/"+fileName;
        return picUrl;
    }
}
