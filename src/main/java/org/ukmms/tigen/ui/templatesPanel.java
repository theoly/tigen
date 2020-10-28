package org.ukmms.tigen.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;
import org.ukmms.tigen.config.Settings;

import javax.swing.*;

/**
 * @author theoly
 * @date 2020/10/28
 */
public class templatesPanel implements Configurable {
    private Settings settings;

    private boolean modified;

    templatesPanel(){
        this.settings = Settings.getInstance();
        this.modified = false;
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Templates";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return null;
    }

    @Override
    public boolean isModified() {
        return this.modified;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
