package org.ukmms.tigen.actions;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theoly
 * @date 2020/10/22
 */
public class DatabaseMenuActionGroup extends ActionGroup {

    @Override
    public @NotNull AnAction[] getChildren(@Nullable AnActionEvent event) {
        Project project = getEventProject(event);
        if (project == null) {
            return AnAction.EMPTY_ARRAY;
        }
        return getSubMenus();
    }

    public AnAction[] getSubMenus(){
        String generatorActionId = "org.ukmms.tigen.action.generate";
        String configActionId = "org.ukmms.tigen.action.config";
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
        // 返回所有菜单
        return new AnAction[]{mainAction, configAction};
    }
}
