package org.ukmms.tigen.service.impl;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import org.ukmms.tigen.domain.DataTable;
import org.ukmms.tigen.domain.Template;
import org.ukmms.tigen.service.TigenService;
import org.ukmms.tigen.util.DataUtils;

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

    /**
     * db info
     */
    private DataUtils dataUtils;

    public TigenServiceImpl(Project project){
        this.project = project;
        this.moduleManager = ModuleManager.getInstance(project);
        this.dataUtils = DataUtils.getInstance();

    }

    @Override
    public String generate(Template template, DataTable dataTable) {
        return null;
    }
}
