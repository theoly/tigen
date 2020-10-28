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
import java.io.IOException;

public class SettingsDialog  implements Configurable, Configurable.Composite {
    private JPanel contentPane;
    private JButton buttonReset;
    private JTextField tfAuthor;
    private JComboBox cbProfile;

    private Settings settings;

    public SettingsDialog() {
        // 获取当前项目
        Project project = ProjectUtils.getCurrProject();

        //初始化事件
        settings = Settings.getInstance();

        tfAuthor.setText(settings.getAuthor());

        settings.getProfileList().forEach(p -> {
            cbProfile.addItem(p);
        });

        cbProfile.setSelectedItem(settings.getProfile());

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
        Configurable[] result = new Configurable[1];
        result[0] = new templatesPanel();
        return result;
    }

    @Override
    public @Nullable JComponent createComponent() {
        return contentPane;
    }

    @Override
    public boolean isModified() {
        boolean modified = false;
        Object selectedItem = cbProfile.getSelectedItem();
        if(!settings.getAuthor().equals(tfAuthor.getText())){
            modified = true;
        }
        if(!settings.getProfile().equals(cbProfile.getSelectedItem().toString())) {
            modified = true;
        }
        return modified;
    }

    @Override
    public void apply() throws ConfigurationException {
        settings.setAuthor(tfAuthor.getText());
        if(!settings.getProfile().equals(cbProfile.getSelectedItem().toString())){
            settings.setProfile(cbProfile.getSelectedItem().toString());
            try {
                settings.setTemplates(Settings.loadProfile(System.getProperty("user.home") + "/.tigen/profile/" + settings.getProfile()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
