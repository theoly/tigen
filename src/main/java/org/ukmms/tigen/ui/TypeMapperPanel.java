package org.ukmms.tigen.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author theoly
 * @date 2020/11/6
 */
public class TypeMapperPanel implements Configurable {
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Type Mapper";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return null;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
