package org.ukmms.tigen.ui;

import javax.swing.*;
import java.awt.*;

/**
 * @author theoly
 * @date 2020/10/28
 */
public class JTemplateField extends JPanel {
    private JCheckBox checkBox;
    private JButton button;
    private String title;

    public JTemplateField(String title){
        this.title = title;
        checkBox = new JCheckBox(title);
        button = new JButton("Preview");
        this.setLayout(new GridLayout(1,2));
        this.add(checkBox);
        this.add(button);
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
