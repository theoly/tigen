package org.ukmms.tigen.ui;

import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.testFramework.LightVirtualFile;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.ukmms.tigen.config.Settings;
import org.ukmms.tigen.util.DataUtils;
import org.ukmms.tigen.util.ModuleUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GenerateDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox cbModule;
    private JTextField tfPackage;
    private JTextField tfPrefix;
    private JPanel panelTempl;
    private List<JTemplateField> templateFieldList = new ArrayList<>();

    private List<Module> moduleList;

    private Settings settings;

    private DataUtils dataUtils;

    public GenerateDialog(Project project) {
        this.settings = Settings.getInstance();
        this.dataUtils = DataUtils.getInstance();

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        this.moduleList = new LinkedList<>();
        for (Module module : ModuleManager.getInstance(project).getModules()) {
            // 存在源代码文件夹放前面，否则放后面
            if (ModuleUtils.existsSourcePath(module)) {
                this.moduleList.add(0, module);
            } else {
                this.moduleList.add(module);
            }
        }

        //初始化Module选择
        for (Module module : this.moduleList) {
            cbModule.addItem(module.getName());
        }

        // templates
        panelTempl.removeAll();
        panelTempl.setLayout(new GridLayout(this.settings.getTemplates().size(),1));
        settings.getTemplates().forEach(t -> {
            JTemplateField templateField = new JTemplateField(t.getName());
            templateField.getButton().addActionListener(e ->{
                try {
                    preview(project, t.getName(), t.getEngine(), t.getCode());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            templateFieldList.add(templateField);
            panelTempl.add(templateField);
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    /**
     * 打开窗口
     */
    public void open() {
        this.pack();
        setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void preview(Project project, String template, String engine, String code) throws IOException {
        String pkg = tfPackage.getText();
        String preName = tfPrefix.getText();

        //初始化代码
        if(engine.equals("beetl")) {
            StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
            Configuration cfg = Configuration.defaultConfiguration();
            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
            Template t = gt.getTemplate(code);
            Map<String, String> map = new HashMap<>();
            map.put("package", pkg);
            map.put("preName", preName);
            t.binding(map);
            code = t.render();
        }

        // 创建编辑框
        EditorFactory editorFactory = EditorFactory.getInstance();
        PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(project);
        String fileName = template;
        FileType velocityFileType = FileTypeManager.getInstance().getFileTypeByExtension("vm");
        PsiFile psiFile = psiFileFactory.createFileFromText(template, velocityFileType, code, 0, true);
        // 标识为模板，让velocity跳过语法校验
        psiFile.getViewProvider().putUserData(FileTemplateManager.DEFAULT_TEMPLATE_PROPERTIES, FileTemplateManager.getInstance(project).getDefaultProperties());
        Document document = PsiDocumentManager.getInstance(project).getDocument(psiFile);
        assert document != null;
        Editor editor = editorFactory.createEditor(document, project, velocityFileType, true);
        // 配置编辑框
        EditorSettings editorSettings = editor.getSettings();
        // 关闭虚拟空间
        editorSettings.setVirtualSpace(false);
        // 关闭标记位置（断点位置）
        editorSettings.setLineMarkerAreaShown(false);
        // 关闭缩减指南
        editorSettings.setIndentGuidesShown(false);
        // 显示行号
        editorSettings.setLineNumbersShown(true);
        // 支持代码折叠
        editorSettings.setFoldingOutlineShown(true);
        // 附加行，附加列（提高视野）
        editorSettings.setAdditionalColumnsCount(3);
        editorSettings.setAdditionalLinesCount(3);
        // 不显示换行符号
        editorSettings.setCaretRowShown(false);
        ((EditorEx) editor).setHighlighter(EditorHighlighterFactory.getInstance().createEditorHighlighter(project, new LightVirtualFile(fileName)));
        // 构建dialog
        DialogBuilder dialogBuilder = new DialogBuilder(project);
        dialogBuilder.setTitle("preview - " + template);
        JComponent component = editor.getComponent();
        component.setPreferredSize(new Dimension(800, 600));
        dialogBuilder.setCenterPanel(component);
        dialogBuilder.addCloseButton();
        dialogBuilder.addDisposable(() -> {
            //释放掉编辑框
            editorFactory.releaseEditor(editor);
            dialogBuilder.dispose();
        });
        dialogBuilder.show();
    }
}
