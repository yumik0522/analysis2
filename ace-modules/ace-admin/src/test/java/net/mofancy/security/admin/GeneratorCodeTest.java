package net.mofancy.security.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import net.mofancy.security.common.constant.UserConstant;
import net.mofancy.security.common.util.GeneratorCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by zwq on 2019/11/19 0019.
 */
public class GeneratorCodeTest {
    public static void main(String[] args) {
        //目录
        GeneratorCode.catalog = "\\ace-modules\\ace-admin";
        //作者
        GeneratorCode.authorName = "zwq";
        //是否覆盖
        GeneratorCode.fileOverride = true;
        //要生成的表名
        GeneratorCode.tables= new String [] {"wg_disc_content"};
        //要生成的代码
        GeneratorCode.codes = new String [] {"xml","mapper","serviceImpl","controller","entity"};
//        GeneratorCode.codes = new String [] {"xml","mapper","entity"};
        //table前缀
        GeneratorCode.prefix="wg_";
        //数据库配置四要素
        GeneratorCode.driverName = "com.mysql.cj.jdbc.Driver";
        GeneratorCode.dburl = "jdbc:mysql://120.24.218.23:3306/weigrass?useUnicode=true&useSSL=false&characterEncoding=utf8";
        GeneratorCode.username = "root";
        GeneratorCode.password = "duan";

        GeneratorCode.moduleName = "admin";

        GeneratorCode.prentPackage = "net.mofancy.security";

        GeneratorCode.idType = IdType.AUTO;
        GeneratorCode.execute();

    }
}
