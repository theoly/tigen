package org.ukmms.tigen.service.impl;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import kotlin.reflect.jvm.internal.impl.name.NameUtils;
import org.apache.commons.lang.StringUtils;
import org.ukmms.tigen.domain.DataTable;
import org.ukmms.tigen.domain.Template;
import org.ukmms.tigen.service.GenerateService;
import org.ukmms.tigen.service.TigenService;
import org.ukmms.tigen.util.DataUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author theoly
 * @date 2020/10/28
 */
public class TigenServiceImpl implements TigenService {
    /**
     * project
     */
    private Project project;

    /**
     * module
     */
    private ModuleManager moduleManager;

    private Map<String, GenerateService> generateServiceMap;

    /**
     * db info
     */
    private DataUtils dataUtils;

    public TigenServiceImpl(Project project){
        this.project = project;
        this.moduleManager = ModuleManager.getInstance(project);
        this.dataUtils = DataUtils.getInstance();

        generateServiceMap = new HashMap<>();
        GenerateService beetlGenerator = new BeetlGenerateServiceImpl();
        GenerateService freemarkerGenerator = new FreemarkerGenerateServiceImpl();
        GenerateService vmGenerator = new VelocityGenerateServiceImpl();
        generateServiceMap.put("beetl", beetlGenerator);
        generateServiceMap.put("freemarker", freemarkerGenerator);
        generateServiceMap.put("velocity", vmGenerator);
    }

    @Override
    public String generate(Template template, DataTable dataTable) {
        GenerateService generateService = generateServiceMap.get(template.getEngine());
        Map<String, Object> param = new HashMap<>();
        // table info to param map
        param.put("dataTable", param);
        param.put("package", "org.ukmms.tigen");
        String renderCode = null;
        try {
            renderCode = generateService.generate(template.getCode(), param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return renderCode;
    }
}
