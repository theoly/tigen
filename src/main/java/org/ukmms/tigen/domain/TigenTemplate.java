package org.ukmms.tigen.domain;

import com.intellij.openapi.util.io.FileUtilRt;
import org.apache.commons.lang3.StringUtils;

/**
 * @author theoly
 * @date 2020/10/28
 */
public class TigenTemplate {
    private String name;
    private String engine;
    private String code;

    public TigenTemplate() {
        this.name = "t.java";
        this.engine = "beetl";
        this.code = "public class T {}";
    }

    public TigenTemplate(String name, String engine, String code) {
        this.name = name;
        this.engine = engine;
        this.code = code;
    }

    public TigenTemplate(String fileName) {
        String extension = FileUtilRt.getExtension(fileName);
        code = "";
        if(StringUtils.isNoneEmpty(extension)){
            name = getTemplateName(fileName, extension);
            switch (extension) {
                case "btl":
                    engine = "beetl";
                    break;
                case "ftl":
                    engine = "freemarker";
                    break;
                case "vm":
                    engine = "velocity";
                    break;
            }
        }else{
            throw new RuntimeException("Template extension must be .btl, .ftl, .vm");
        }
    }

    public static String getTemplateName(String fileName, String extension){
        return fileName.substring(0, extension.length()+1);
    }

    public String getFileName(){
        return name + "." + getFileExt();
    }

    public String getFileExt(){
        String ext = "";
        switch (engine) {
            case "beetl":
                ext = "btl";
                break;
            case "freemarker":
                ext = "ftl";
                break;
            case "velocity":
                ext = "vm";
                break;
        }

        return ext;
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
