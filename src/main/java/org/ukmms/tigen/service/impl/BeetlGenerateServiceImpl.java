package org.ukmms.tigen.service.impl;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.ukmms.tigen.service.GenerateService;

import java.io.IOException;
import java.util.Map;

/**
 * @author theoly
 * @date 2020/10/29
 */
public class BeetlGenerateServiceImpl implements GenerateService {
    @Override
    public String generate(String template, Map<String, Object> param) throws IOException {
        //初始化代码
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        //获取模板
        Template t = gt.getTemplate(template);
        t.binding(param);
        //渲染结果
        String str = t.render();
        return str;
    }
}
