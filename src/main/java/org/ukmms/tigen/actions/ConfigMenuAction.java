package org.ukmms.tigen.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.NlsActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ukmms.tigen.util.DataUtils;

/**
 * @author theoly
 * @date 2020/10/22
 */
public class ConfigMenuAction extends AnAction {
    private Logger logger = Logger.getInstance(GeneratorMenuAction.class);
    private DataUtils dataUtils = DataUtils.getInstance();

    public ConfigMenuAction(@Nullable @NlsActions.ActionText String text) {
        super(text);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        logger.info(dataUtils.getDbTable().getName());
    }
}
