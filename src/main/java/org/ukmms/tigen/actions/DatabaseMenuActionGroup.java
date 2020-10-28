package org.ukmms.tigen.actions;

import com.intellij.database.psi.DbTable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ukmms.tigen.util.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author theoly
 * @date 2020/10/22
 */
public class DatabaseMenuActionGroup extends ActionGroup {

    private Boolean isDataTable = false;

    private DataUtils dataUtils = DataUtils.getInstance();

    @Override
    public boolean hideIfNoVisibleChildren() {
        return !isDataTable;
    }

    @Override
    public @NotNull AnAction[] getChildren(@Nullable AnActionEvent event) {
        Project project = getEventProject(event);
        if (project == null) {
            return AnAction.EMPTY_ARRAY;
        }

        //获取选中的PSI元素
        PsiElement psiElement = event.getData(LangDataKeys.PSI_ELEMENT);
        DbTable selectDbTable = null;
        if (psiElement instanceof DbTable) {
            selectDbTable = (DbTable) psiElement;
        }
        if (selectDbTable == null) {
            isDataTable = false;
            return AnAction.EMPTY_ARRAY;
        }else{
            isDataTable = true;
            dataUtils.setDbTable(selectDbTable);
        }

        //获取选中的所有表
        PsiElement[] psiElements = event.getData(LangDataKeys.PSI_ELEMENT_ARRAY);
        if (psiElements == null || psiElements.length == 0) {
            return AnAction.EMPTY_ARRAY;
        }
        List<DbTable> dbTableList = new ArrayList<>();
        for (PsiElement element : psiElements) {
            if (!(element instanceof DbTable)) {
                continue;
            }
            DbTable dbTable = (DbTable) element;
            dbTableList.add(dbTable);
        }
        if (dbTableList.isEmpty()) {
            isDataTable = false;
            return AnAction.EMPTY_ARRAY;
        }else{
            isDataTable = true;
            dataUtils.setDbTables(dbTableList);
        }

        return getSubMenus();
    }

    public AnAction[] getSubMenus(){
        String generatorActionId = "org.ukmms.tigen.action.generate";
        String configActionId = "org.ukmms.tigen.action.config";
        String previewActionId = "org.ukmms.tigen.action.preview";
        ActionManager actionManager = ActionManager.getInstance();
        // 代码生成菜单
        AnAction mainAction = actionManager.getAction(generatorActionId);
        if (mainAction == null) {
            mainAction = new GeneratorMenuAction("Generator");
            actionManager.registerAction(generatorActionId, mainAction);
        }
        // 表配置菜单
        AnAction configAction = actionManager.getAction(configActionId);
        if (configAction == null) {
            configAction = new ConfigMenuAction("Config");
            actionManager.registerAction(configActionId, configAction);
        }

        // 表配置菜单
        AnAction previewAction = actionManager.getAction(previewActionId);
        if (previewAction == null) {
            previewAction = new ConfigMenuAction("Preview");
            actionManager.registerAction(previewActionId, previewAction);
        }
        // 返回所有菜单
        return new AnAction[]{mainAction, configAction, previewAction};
    }
}
