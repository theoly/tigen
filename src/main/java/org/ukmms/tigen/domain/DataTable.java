package org.ukmms.tigen.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intellij.database.psi.DbTable;

import java.util.List;
import java.util.Map;

/**
 * @author theoly
 * @date 2020/10/22
 */
public class DataTable {
    /**
     * 原表对象
     */
    @JsonIgnore
    private DbTable dbTable;

    /**
     * 表名
     */
    private String name;

    /**
     * 注释
     */
    private String comment;

    /**
     * 字段集合
     */
    private List<DataColumn> columns;

    /**
     * 扩展数据
     */
    private Map<String, Object> ext;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<DataColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DataColumn> columns) {
        this.columns = columns;
    }
}
