package org.ukmms.tigen.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;
import org.ukmms.tigen.config.Settings;
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
public class profilePanel implements Configurable {
    private Project project;

    private Settings settings;

    private boolean modified;

    private TemplateListBox listBox;

    private TemplateEditor templateEditor;

    profilePanel(){
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

        settings.getProfile().getTemplates().forEach(t -> {
            listBox.addItem(t.getFileName());
        });

        Splitter splitter = new Splitter(true, 0.3F);
        splitter.setFirstComponent(listBox.createComponent());
        splitter.setSecondComponent(new TemplateEditor(project, new TigenTemplate()).createComponent());

        mainPanel.add(splitter, BorderLayout.CENTER);

//        mainPanel.add(listBox.createComponent());
//
//        mainPanel.add(new TemplateEditor(project, new TigenTemplate()).createComponent());
        return mainPanel;
    }

    @Override
    public boolean isModified() {
        return this.modified;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
