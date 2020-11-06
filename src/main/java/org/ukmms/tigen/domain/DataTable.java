package org.ukmms.tigen.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intellij.database.model.DasColumn;
import com.intellij.database.psi.DbTable;
import com.intellij.database.util.DasUtil;
import com.intellij.util.containers.JBIterable;
import org.ukmms.tigen.util.NameUtils;

import java.util.ArrayList;
import java.util.HashMap;
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

    public DataTable(DbTable dbTable) {
        this.dbTable = dbTable;
        this.name = dbTable.getName();
        this.comment = dbTable.getComment();

        this.ext = new HashMap<>();

        this.ext.put("className", NameUtils.getClassName(this.name));
        this.ext.put("entityName", NameUtils.getEntityName(this.name));

        JBIterable<? extends DasColumn> columns = DasUtil.getColumns(dbTable);
        this.columns = new ArrayList<>();
        for (DasColumn column : columns) {
            this.columns.add(getColumnInfo(column));
        }
    }

    private DataColumn getColumnInfo(DasColumn dasColumn){
        DataColumn column = new DataColumn();
        column.setDasColumn(dasColumn);
        column.setName(dasColumn.getName());
        column.setComment(dasColumn.getComment());
        column.setType(dasColumn.getDataType().getSpecification());

        column.getExt().put("propName", NameUtils.getEntityName(column.getName()));

        return column;
    }

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

    public DbTable getDbTable() {
        return dbTable;
    }

    public void setDbTable(DbTable dbTable) {
        this.dbTable = dbTable;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }

    @Override
    public String toString() {
        return "DataTable{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
