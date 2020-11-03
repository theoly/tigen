package org.ukmms.tigen.domain;

/**
 * @author theoly
 * @date 2020/10/28
 */
public class TigenTemplate {
    private String name;
    private String engine;
    private String code;

    public TigenTemplate() {
    }

    public TigenTemplate(String name, String engine, String code) {
        this.name = name;
        this.engine = engine;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
