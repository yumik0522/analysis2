package net.mofancy.security.admin.rest;

import net.mofancy.security.common.util.AliyunOssHelper;
import net.mofancy.security.common.constant.FileTypeEnum;
import net.mofancy.security.common.exception.common.ParameterIllegalException;
import net.mofancy.security.common.web.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zwq
 * @version 1.0
 * @date 2019/12/9 0009 下午 2:59
 */
@RestController
@RequestMapping("file")
public class FileController {


    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 上传文件
     * @author zwq
     * @date 2019/12/9 0009
     * @param file
     * @return net.mofancy.security.common.web.ApiResponse
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ApiResponse uploadFile(MultipartFile file) {

        log.info("进入上传文件文件接口");

        Map<String, Object> resultMap = new HashMap<>();
        String url = "";
        if (!StringUtils.isEmpty(file) && file.getSize() > 0) {
            byte[] bytes = null;
            try {
                bytes = file.getBytes();
            } catch (IOException e) {
                throw new ParameterIllegalException("获取上传文件文件失败！");
            }
            String originalFilename = file.getOriginalFilename();
            //获取小数点的角标
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //获取小数点后面的后缀名
            String extension = originalFilename.substring(lastIndexOf + 1);
            FileTypeEnum fileTypeEnum = new FileTypeEnum();
            //Boolean flag = false;
            fileTypeEnum.setFileType(FileTypeEnum.FileType.valueOf(extension.toUpperCase()));
            String type = fileTypeEnum.getFileTypeName();
            //上传文件
            switch (type) {
                case "pic":url = AliyunOssHelper.uploadPic(bytes, extension);break;
                case "doc":url = AliyunOssHelper.uploadDoc(bytes, extension);break;
                case "audio":url = AliyunOssHelper.uploadAudio(bytes, extension);break;
                case "video":url = AliyunOssHelper.uploadVideo(bytes, extension);break;
                default:throw new ParameterIllegalException("文件上传失败，非法文件类型！");
            }

            if (StringUtils.isEmpty(url)) {
                throw new ParameterIllegalException("上传文件返回地址为空！");
            }
        }
        resultMap.put("url",url);
        return ApiResponse.buildSuccess(resultMap);
    }

}
