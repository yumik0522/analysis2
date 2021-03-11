package net.mofancy.security.admin.config;

/**
 * @author zwq
 * @version 1.0
 * @description 动态数据源上下文管理：设置数据源，获取数据源，清除数据源
 * @date 2020/3/9 0009 下午 4:36
 */
public class DataSourceContextHolder {

    /**
     * 默认数据源
     */
    public static final String DEFAULT_DS = "";

    // 存放当前线程使用的数据源类型
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    // 设置数据源
    public static void setDataSource(String type){
        contextHolder.set(type);
    }

    // 获取数据源
    public static String getDataSource(){
        return contextHolder.get();
    }

    // 清除数据源
    public static void clearDataSource(){
        contextHolder.remove();
    }
}
