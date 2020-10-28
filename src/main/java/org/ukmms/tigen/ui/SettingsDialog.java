package org.ukmms.tigen.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ukmms.tigen.config.Settings;
import org.ukmms.tigen.util.ProjectUtils;

import javax.swing.*;

public class SettingsDialog  implements Configurable, Configurable.Composite {
    private JPanel contentPane;
    private JButton buttonReset;
    private JTextField tfAuthor;
    private JTextField tfProfile;

    private Settings settings;

    public SettingsDialog() {
        // 获取当前项目
        Project project = ProjectUtils.getCurrProject();

        //初始化事件
        settings = Settings.getInstance();

        tfAuthor.setText(settings.getAuthor());

        buttonReset.addActionListener(e ->{
            settings = new Settings();
        });
    }


    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "TiGen";
    }

    @Override
    public Configurable @NotNull [] getConfigurables() {
        return new Configurable[0];
    }

    @Override
    public @Nullable JComponent createComponent() {
        return contentPane;
    }

    @Override
    public boolean isModified() {
        boolean modified = false;
        if(!settings.getAuthor().equals(tfAuthor.getText())){
            modified = true;
        }
        return modified;
    }

    @Override
    public void apply() throws ConfigurationException {
        settings.setAuthor(tfAuthor.getText());
    }
}
