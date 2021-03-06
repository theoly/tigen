package org.ukmms.tigen.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.Nullable;
import org.ukmms.tigen.config.Settings;
import org.ukmms.tigen.domain.TigenProfile;
import org.ukmms.tigen.domain.TigenTemplate;
import org.ukmms.tigen.ui.component.TemplateEditor;
import org.ukmms.tigen.ui.component.TemplateListBox;
import org.ukmms.tigen.util.ProjectUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author theoly
 * @date 2020/10/28
 */
public class ProfilePanel implements Configurable {
    private Project project;

    private Settings settings;

    private boolean modified;

    private TemplateListBox listBox;

    private TemplateEditor templateEditor;

    ProfilePanel() {
        this.project = ProjectUtils.getCurrProject();
        this.settings = Settings.getInstance();
        this.modified = false;
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "profiles";
    }

    @Override
    public @Nullable JComponent createComponent() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        listBox = new TemplateListBox();
        templateEditor = new TemplateEditor(project);

        settings.getProfile().getTemplates().forEach(t -> {
            listBox.addItem(t.getFileName());
        });

        listBox.getListPanel().addListSelectionListener(e -> {
            onUpdate();
        });


        Splitter splitter = new Splitter(false, 0.2F);
        splitter.setFirstComponent(listBox.createComponent());

        splitter.setSecondComponent(templateEditor.createComponent());


        mainPanel.add(splitter, BorderLayout.CENTER);

//        mainPanel.add(listBox.createComponent());
//
//        mainPanel.add(new TemplateEditor(project, new TigenTemplate()).createComponent());
        return mainPanel;
    }

    /**
     * 数据发生修改时调用
     */
    private void onUpdate() {
        String templateName = listBox.getListPanel().getSelectedValue();
        TigenTemplate template = new TigenTemplate();
        for (TigenTemplate tigenTemplate : settings.getProfile().getTemplates()) {
            if (tigenTemplate.getName().equals(templateName)) {
                template = tigenTemplate;
            }
        }
        templateEditor.setTemplate(template);

    }

    @Override
    public boolean isModified() {
        if(templateEditor.getTemplate()!= null && !templateEditor.getTemplate().getCode().equals(templateEditor.getEditor().getDocument().getText())){
            this.modified = true;
        }
        return this.modified;
    }

    @Override
    public void apply() throws ConfigurationException {
        TigenTemplate template = templateEditor.getTemplate();
        template.setCode(templateEditor.getEditor().getDocument().getText());
        settings.getProfile().getTemplates().forEach(t -> {
            if(t.getName().equals(template.getName()) && t.getEngine().equals(template.getEngine())) {
                t.setCode(templateEditor.getEditor().getDocument().getText());
            }
        });
        settings.saveProfile(settings.getProfile());

        this.modified = false;
    }

    @Override
    public void disposeUIResources() {
        if (templateEditor != null) {
            templateEditor.onClose();
        }
    }
}
