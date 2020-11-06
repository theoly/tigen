package org.ukmms.tigen.service.impl;

import com.intellij.database.psi.DbTable;
import com.intellij.openapi.project.Project;
import org.ukmms.tigen.domain.DataTable;
import org.ukmms.tigen.service.DatabaseService;

/**
 * @author theoly
 * @date 2020/11/6
 */
public class DatabaseServiceImpl implements DatabaseService {
    private Project project;

    public DatabaseServiceImpl(Project project) {
        this.project = project;
    }

    @Override
    public DataTable getTableInfo(DbTable dbTable) {
        if (dbTable == null) {
            return null;
        }
        DataTable tableInfo = new DataTable(dbTable);
        return tableInfo;
    }
}
