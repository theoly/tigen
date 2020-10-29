package org.ukmms.tigen.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.diagnostic.Logger;
import org.ukmms.tigen.config.Settings;
import org.ukmms.tigen.ui.GenerateDialog;
import org.ukmms.tigen.util.DataUtils;

import java.io.IOException;

/**
 * @author theoly
 * @date 2020/10/22
 */
public class GeneratorMenuAction extends AnAction {
    private Logger logger = Logger.getInstance(GeneratorMenuAction.class);
    private DataUtils dataUtils = DataUtils.getInstance();

    public GeneratorMenuAction(@Nullable @NlsActions.ActionText String text) {
        super(text);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }

        new GenerateDialog(event.getProject()).open();
    }
}
