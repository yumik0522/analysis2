package net.mofancy.security.admin.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hayson
 * @description
 */
@EnableTransactionManagement
@Configuration
public class DataSourceConfig  {

    public static Map<String,String> dbMap = new HashMap<>();
    // 配置多数据源
    public static Map<Object, Object> dsMap = new HashMap<>();

    private static DataSource mainDb;

    @Bean("mainDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.maindb")
    public DataSource mainDataSource() {
        DataSource build = DataSourceBuilder.create().build();
        return build;
    }

    public DataSource getDataSource(String url,String username,String password)  {

        DataSource ds =  DataSourceBuilder.create().driverClassName("org.postgresql.Driver").password(password).url(url).username(username).build();

        return ds;
    }

    @Bean
    public DynamicDataSource dataSource(@Qualifier("mainDataSource") DataSource mainDataSource){

        this.mainDb = mainDataSource;
        List<Map<String,Object>> list = getDataPoolList(null);

        // 添加的key为String类型
        for (Map<String, Object> map : list) {
            String temp = "jdbc:postgresql://LOCALHOST:5432/DBNAME?currentSchema=SCHEMA&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&useSSL=false";
            String url = temp.replace("LOCALHOST", map.get("dburl").toString()).replace("DBNAME", map.get("dbname").toString()).replace("SCHEMA", map.get("dbschema").toString());
            dsMap.put(map.get("dataset_name").toString(), getDataSource(url,"postgres".toString(),map.get("dbpwd").toString()));
            dbMap.put(map.get("dataset_key").toString(), map.get("dataset_name").toString());
        }
        // 通过单例获取对象
        DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();
        dynamicDataSource.setTargetDataSources(dsMap);
        dynamicDataSource.setDefaultTargetDataSource(mainDataSource);

        return dynamicDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dynamicDataSource);
        factoryBean.setPlugins(new Interceptor[]{paginationInterceptor()});
//        factoryBean.setTypeAliasesPackage();
        // 设置mapper.xml的位置路径
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:mapper/admin/*.xml");
        factoryBean.setMapperLocations(resources);
        return factoryBean.getObject();
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("postgresql");
        return page;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DynamicDataSource dynamicDataSource){
        return new DataSourceTransactionManager(dynamicDataSource);
    }


    public List<Map<String,Object>> getDataPoolList(Integer id) {

        List<Map<String,Object>> list = new ArrayList<>();
        try {
            Connection conn = mainDb.getConnection();
            String sql = "SELECT a.dataset_sys_key AS dataset_key,a.dataset_name,b.param_val AS dbname,c.param_val AS dburl,COALESCE(d.param_val,'public') AS dbschema,COALESCE(e.param_val,'postgres') AS dbpwd FROM dataset_list a\r\n" +
                    "INNER JOIN dataset_parameter b ON\r\n" +
                    "a.dataset_sys_key = b.dataset_sys_key AND b.param_name = 'DatabaseName'\r\n" +
                    "INNER JOIN dataset_parameter c ON\r\n" +
                    "a.dataset_sys_key = c.dataset_sys_key AND c.param_name = 'DatabaseServer'\r\n" +
                    " LEFT JOIN dataset_parameter d ON\r\n" +
                    " a.dataset_sys_key = d.dataset_sys_key AND d.param_name = 'DatabaseSchema'\r\n" +
                    "  LEFT JOIN dataset_parameter e ON\r\n" +
                    " a.dataset_sys_key = e.dataset_sys_key AND e.param_name = 'DatabasePwd' WHERE 1=1 ";
            if(id!=null) {
                sql += " AND a.dataset_sys_key = "+id+" ";
            }
            PreparedStatement pstmt;

            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsm =rs.getMetaData();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Map<String,Object> map = new HashMap<String,Object>();
                for (int i = 1; i <= col; i++) {
                    String value = rs.getString(i);
                    map.put(rsm.getColumnName(i), value);
                }
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getJobId() {
        int id = 0;
        String sql = "SELECT nextval('jqueue_job_seq')";
        PreparedStatement pstmt;

        try {
            Connection conn = mainDb.getConnection();
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                id = Integer.parseInt(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
