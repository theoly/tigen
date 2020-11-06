package org.ukmms.tigen.service;

import com.intellij.database.psi.DbTable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.ukmms.tigen.domain.DataTable;

/**
 * @author theoly
 * @date 2020/11/6
 */
public interface DatabaseService {
    static DatabaseService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, DatabaseService.class);
    }

    public DataTable getTableInfo(DbTable dbTable);
}
