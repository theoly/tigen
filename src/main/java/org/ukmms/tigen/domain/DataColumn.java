package org.ukmms.tigen.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intellij.database.model.DasColumn;

import java.util.Map;

/**
 * @author theoly
 * @date 2020/10/22
 */
public class DataColumn {

    /**
     * 原字段对象
     */
    @JsonIgnore
    private DasColumn dasColumn;

    /**
     * 字段名
     */
    private String name;
    /**
     * 注释
     */
    private String comment;
    /**
     * 类型
     */
    private String type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
