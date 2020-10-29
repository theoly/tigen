package org.ukmms.tigen.service.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.ukmms.tigen.service.GenerateService;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * @author theoly
 * @date 2020/10/29
 */
public class FreemarkerGenerateServiceImpl implements GenerateService {

    @Override
    public String generate(String template, Map<String, Object> param) throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
        Template freemarkerTemplate = new Template("freemarker", template, cfg);
        Writer out = new StringWriter();
        freemarkerTemplate.process(param, out);
        out.flush();
        String s = out.toString();
        out.close();
        return s;
    }
}
