package org.ukmms.tigen.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.ukmms.tigen.domain.DataTable;
import org.ukmms.tigen.domain.TigenTemplate;

/**
 * @author theoly
 * @date 2020/10/28
 */
public interface TigenService {
    /**
     * 获取实例对象
     *
     * @param project 项目对象
     * @return 实例对象
     */
    static TigenService getInstance(Project project){
        return ServiceManager.getService(project, TigenService.class);
    }

    /**
     * 生成代码
     *
     * @param tigenTemplate  模板
     * @param dataTable 表信息对象
     * @return 生成好的代码
     */
    String generate(TigenTemplate tigenTemplate, DataTable dataTable);
}
