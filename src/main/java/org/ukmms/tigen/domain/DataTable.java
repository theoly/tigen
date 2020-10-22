package org.ukmms.tigen.domain;

import java.util.List;

/**
 * @author theoly
 * @date 2020/10/22
 */
public class DataTable {
    private String name;

    private String comment;

    private List<DataColumn> columns;

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
