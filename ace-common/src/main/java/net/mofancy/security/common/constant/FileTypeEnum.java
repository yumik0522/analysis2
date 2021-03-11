package net.mofancy.security.common.constant;

import com.baomidou.mybatisplus.generator.config.rules.FileType;

/**
 * @author zwq
 * @version 1.0
 * @date 2019/12/9 0009 下午 3:20
 */
public class FileTypeEnum {

    private FileType fileType;

    public enum FileType {
        PNG("png","pic"),JPG("jpg","pic"),GIF("gif","pic"),
        XLS("xls","doc"),XLSX("xlsx","doc"),TXT("txt","doc"),CSV("csv","doc"),PDF("pdf","doc"),DOCX("docx","doc"),DOC("doc","doc"),PPT("ppt","doc"),PPTX("pptx","doc"),
        MP3("mp3","audio"),WMA("wma","audio"),
        FLV("flv","video"),RMVB("rmvb","video"),MP4("mp4","video"),MVB("mvb","video");
        // 成员变量
        private final String name;
        private final String type;

        FileType(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    //返回枚举类型的name属性
    public String getFileTypeName() {
        return fileType != null ? this.fileType.getType() : "";
    }

}
