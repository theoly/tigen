package org.ukmms.tigen.actions;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.NlsActions;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

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
        PsiElement data = e.getData(LangDataKeys.PSI_ELEMENT);

        if(data.getLanguage().isKindOf(JavaLanguage.INSTANCE)){
            e.getPresentation().setVisible(true);
        }else {
            e.getPresentation().setVisible(false);
        }

        super.update(e);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        PsiElement data = event.getData(LangDataKeys.PSI_ELEMENT);
        if (!(data instanceof PsiClass)) {
            Messages.showMessageDialog(event.getProject(), "Please focus on a class", "Generate Failed", null);
            return;
        }



        PsiClass psiClass = (PsiClass) data;
        Map<String, Object> classMap = new HashMap<>();
        classMap.put("className", psiClass.getName());

        Map<String, String> fieldMap = new HashMap<>();
        List<PsiField> fields = getFields(psiClass);
        for (PsiField field : fields) {
            String javaName = field.getName();
            String javaType = field.getType().getCanonicalText();
            fieldMap.put(javaName, javaType);
        }
        classMap.put("fields", fieldMap);

        logger.info("generate ddl");
    }

    public List<PsiField> getFields(PsiClass psiClass){
        List<PsiField> ret = new ArrayList<>();
        PsiField[] fields = psiClass.getFields();

        PsiMethod[] methods = psiClass.getAllMethods();
        List<String> getters = Arrays.stream(methods).map(m -> {
            String methodName = m.getName();
            if (methodName.startsWith("get")) {
                String getterName = getFieldNameFromGetterName(methodName);
                return getterName;
            }
            return null;
        }).collect(Collectors.toList());
        for (PsiField field : fields) {
            if (getters.contains(field.getName())){
                ret.add(field);
            }
        }

        return ret;
    }

    public static String getFieldNameFromGetterName(String methodName){
        String head = methodName.substring(3, 4);
        return head.toLowerCase() + methodName.substring(4);
    }


}

