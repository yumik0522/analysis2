package net.mofancy.security.admin.vo;

/**
 * @author Hayson
 * @description 列出所有数据源
 */
public enum DataSourceType {
    db1("db1"),
    db2("db2");
    private String db;
    DataSourceType(String db) {
        this.db = db;
    }
    public String getDb() {
        return db;
    }
    public void setDb(String db) {
        this.db = db;
    }
}
