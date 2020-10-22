package org.ukmms.tigen.util;

import com.intellij.database.psi.DbTable;

import java.util.List;

/**
 * @author theoly
 * @date 2020/10/22
 */
public class DataUtils {
    private volatile static DataUtils dataUtils;

    /**
     * singleton
     * @return DataUtils instance
     */
    public static DataUtils getInstance(){
        if (dataUtils == null) {
            synchronized (DataUtils.class) {
                if (dataUtils == null) {
                    dataUtils = new DataUtils();
                }
            }
        }
        return dataUtils;
    }

    private DbTable dbTable;

    private List<DbTable> dbTables;

    public DbTable getDbTable() {
        return dbTable;
    }

    public void setDbTable(DbTable dbTable) {
        this.dbTable = dbTable;
    }

    public List<DbTable> getDbTables() {
        return dbTables;
    }

    public void setDbTables(List<DbTable> dbTables) {
        this.dbTables = dbTables;
    }
}
