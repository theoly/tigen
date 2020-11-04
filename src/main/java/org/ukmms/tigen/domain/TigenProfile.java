package org.ukmms.tigen.domain;

import com.intellij.util.xmlb.annotations.Transient;

import java.util.List;

/**
 * @author theoly
 * @date 2020/11/3
 */
public class TigenProfile {
    private String name;
    private String path;

    private List<TigenTemplate> templates;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Transient
    public List<TigenTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<TigenTemplate> templates) {
        this.templates = templates;
    }
}
