package org.ukmms.tigen.ui.component;

import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.UnknownFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.testFramework.LightVirtualFile;
import org.ukmms.tigen.domain.TigenTemplate;
import org.ukmms.tigen.event.TemplateChangeEvent;
import org.ukmms.tigen.event.TemplateChangeEventListener;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.util.Date;

/**
 * @author theoly
 * @date 2020/11/4
 */
public class TemplateEditor {
    /**
     * project obj
     */
    private Project project;

    /**
     * template obj
     */
    private TigenTemplate template;

    /**
     * idea editor
     */
    private Editor editor;

    /**
     * panel
     */
    private JPanel panel;

    protected EventListenerList listenerList = new EventListenerList();

    public void addTemplateChangeEventListener(TemplateChangeEventListener eventListener) {
        listenerList.add(TemplateChangeEventListener.class, eventListener);
    }

    public void removeTemplateChangeEventListener(TemplateChangeEventListener eventListener) {
        listenerList.remove(TemplateChangeEventListener.class, eventListener);
    }

    void fireTemplateChangeEvent(TemplateChangeEvent event){
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i+2) {
            if (listeners[i] == TemplateChangeEventListener.class) {
                ((TemplateChangeEventListener) listeners[i+1]).onTemplateChange(event);
            }
        }
    }

    public TemplateEditor(Project project) {
        this.project = project;
    }

    public TemplateEditor(Project project, TigenTemplate template) {
        this.project = project;
        this.template = template;
    }


    public JComponent createComponent() {

        // panel
        panel = new JPanel(new BorderLayout());

        createEditor();

        return panel;
    }

    /**
     * on close event
     */
    public void onClose() {
        if (editor != null) {
            EditorFactory.getInstance().releaseEditor(editor);
        }
        editor = null;
    }

    public void createEditor(){
        if( template != null) {
            if (editor == null) {

                EditorFactory editorFactory = EditorFactory.getInstance();
                if (editor != null) {
                    editorFactory.releaseEditor(editor);
                }

                FileType fileType = FileTypeManager.getInstance().getFileTypeByExtension(template.getFileExt());
                if (fileType instanceof UnknownFileType) {
                    String fileExt = FileUtilRt.getExtension(template.getName());
                    fileType = FileTypeManager.getInstance().getFileTypeByExtension(fileExt);
                }

                PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(project);
                PsiFile psiFile = psiFileFactory
                        .createFileFromText(template.getName(), fileType, template.getCode(), new Date().getTime(), true);
                psiFile.getViewProvider()
                        .putUserData(FileTemplateManager.DEFAULT_TEMPLATE_PROPERTIES, FileTemplateManager.getInstance(project).getDefaultProperties());
                // 创建文档对象
                Document document = PsiDocumentManager.getInstance(project).getDocument(psiFile);
                assert document != null;
                // 创建编辑框
                editor = editorFactory.createEditor(document, project);

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

                EditorHighlighter editorHighlighter = EditorHighlighterFactory.getInstance()
                        .createEditorHighlighter(project, new LightVirtualFile(template.getName()));
                ((EditorEx) editor).setHighlighter(editorHighlighter);

                panel.add(editor.getComponent(), BorderLayout.CENTER);
            } else {
                WriteCommandAction.runWriteCommandAction(project, () -> this.editor.getDocument().setText(template.getCode()));
            }
        }
    }


    public TigenTemplate getTemplate() {
        return template;
    }

    public void setTemplate(TigenTemplate template) {
        this.template = template;

        createEditor();
        fireTemplateChangeEvent(new TemplateChangeEvent(this));
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }
}
