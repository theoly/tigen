package org.ukmms.tigen.domain;

/**
 * @author theoly
 * @date 2020/11/6
 */
public class TypeMapper {
    private String dbType;
    private String javaType;

    public TypeMapper() {
    }

    public TypeMapper(String dbType, String javaType) {
        this.dbType = dbType;
        this.javaType = javaType;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }
}
