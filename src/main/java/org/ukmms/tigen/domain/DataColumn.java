package org.ukmms.tigen.domain;

/**
 * @author theoly
 * @date 2020/10/22
 */
public class DataColumn {
    private String name;
    private String comment;
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
