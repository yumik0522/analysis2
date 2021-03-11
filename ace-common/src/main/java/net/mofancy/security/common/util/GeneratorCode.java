package net.mofancy.security.common.util;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwq on 2019/10/31 0031.
 */
public class GeneratorCode {

    //作者
    public static String authorName="zhangweiqi";
    //是否覆盖
    public static boolean fileOverride = false;
    //要生成的表名
//    private static String[] tables= {"client","base_element","gate_log","base_group","member","base_user","base_resource_authority","base_menu","base_group_type"};
    public static String[] tables= {"member"};
    //要生成的代码
    public static String [] codes = {"xml","mapper","serviceImpl","controller","entity"};
    //table前缀
    public static String prefix="base_";

    //数据库配置四要素
    public static String driverName = "com.mysql.cj.jdbc.Driver";
    public static String dburl = "jdbc:mysql://localhost:3306/weigrass?useUnicode=true&useSSL=false&characterEncoding=utf8";
    public static String username = "root";
    public static String password = "123456";

    public static String moduleName = "shop";

    public static String prentPackage = "net.mofancy.security";

    public static IdType idType = IdType.AUTO;

    public static String catalog = "";

    /**
     * mybatis-plus代码自动生成
     * @author zwq
     * @date 2019/11/19 0019
     * @param []
     * @return void
     */
    public static void execute() {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();

        String projectPath = System.getProperty("user.dir")+catalog;

        System.out.println(projectPath);
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(authorName);
        gc.setFileOverride(fileOverride);
        gc.setOpen(false);
        gc.setBaseResultMap(true);
        gc.setServiceImplName("%sBiz");
        gc.setDateType(DateType.ONLY_DATE);
        gc.setIdType(idType);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(dburl);
        // dsc.setSchemaName("public");
        dsc.setDriverName(driverName);
        dsc.setUsername(username);
        dsc.setPassword(password);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(moduleName);
        pc.setParent(prentPackage).setController("rest").setServiceImpl("biz");

        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        String templateCode = "";
        for (String code: codes ) {
            templateCode+=code;
        }
        if(templateCode.indexOf("xml")<0) {
            templateConfig.setXml(null);
        }
        if(templateCode.indexOf("mapper")<0) {
            templateConfig.setMapper(null);
        }
        if(templateCode.indexOf("iService")<0) {
            templateConfig.setService(null);
        }
        if(templateCode.indexOf("serviceImpl")<0) {
            templateConfig.setServiceImpl(null);
        }
        if(templateCode.indexOf("controller")<0) {
            templateConfig.setController(null);
        }
        if(templateCode.indexOf("entity")<0) {
            templateConfig.setEntity(null);
        }


        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass("");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        strategy.setSuperControllerClass("BaseController");
        strategy.setSuperServiceImplClass("BaseBiz");


        // 写于父类中的公共字段
//        strategy.setSuperEntityColumns("id");
        strategy.setInclude(tables);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(prefix);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
