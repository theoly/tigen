package org.ukmms.tigen.actions;

import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsActions;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theoly
 * @date 2021/1/12
 */
public class GenerateDdlMenuAction  extends AnAction {
    private Logger logger = Logger.getInstance(GenerateDdlMenuAction.class);

    private PsiFile psiFile;

    public GenerateDdlMenuAction() {
        super("Generate DDL");
    }

    public GenerateDdlMenuAction(@Nullable @NlsActions.ActionText String text) {
        super(text);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {

        PsiFile file = e.getData(LangDataKeys.PSI_FILE);

        String name = file.getLanguage().getID();
        if(name.equals("JAVA")){
            e.getPresentation().setVisible(true);
        }else {
            e.getPresentation().setVisible(false);
        }

        super.update(e);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        PsiFile file = event.getData(LangDataKeys.PSI_FILE);
        
        logger.info("generate ddl");
    }
}

