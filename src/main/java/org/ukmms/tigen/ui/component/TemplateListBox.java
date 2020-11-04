package org.ukmms.tigen.ui.component;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.border.CustomLineBorder;
import com.intellij.ui.components.JBList;
import org.ukmms.tigen.domain.TigenTemplate;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author theoly
 * @date 2020/11/4
 */
public class TemplateListBox{
    private Logger logger = Logger.getInstance(TemplateListBox.class);

    private List<TigenTemplate> templates;

    private JBList<String> listPanel;

    public TemplateListBox(){
        templates = new ArrayList<>();
        listPanel = new JBList<>(getModel());
    }

    public void addItem(String fileName) {
        TigenTemplate template = new TigenTemplate(fileName);
        templates.add(template);
        refresh();
    }

    private void refresh(){
        listPanel.setModel(getModel());
        listPanel.setSelectedIndex(templates.size() -1);
    }

    private ListModel<String> getModel(){
        List<String> ret = new ArrayList<>();
        templates.forEach(t -> {
            ret.add(t.getName());
        });

        return new CollectionListModel<String>(ret);
    }

    public JComponent createComponent(){
        JPanel mainPanel = new JPanel(new BorderLayout());

        listPanel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPanel.addListSelectionListener(e -> {
            logger.info("Item selected " + e.toString());
        });

        listPanel.setBorder(new CustomLineBorder(0, 1, 1, 1));
        mainPanel.add(listPanel, BorderLayout.CENTER);
        return mainPanel;
    }




    public List<TigenTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<TigenTemplate> templates) {
        this.templates = templates;
    }

    public JBList<String> getListPanel() {
        return listPanel;
    }

    public void setListPanel(JBList<String> listPanel) {
        this.listPanel = listPanel;
    }
}
