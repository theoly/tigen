package org.ukmms.tigen.ui.component;

import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.ui.JBUI;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.jetbrains.annotations.NotNull;
import org.ukmms.tigen.domain.TigenTemplate;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Objects;

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

    private Callback callback;

    public TemplateEditor(Project project, TigenTemplate template) {
        this.project = project;
        this.template = template;
    }


    public JComponent createComponent() {
        EditorFactory editorFactory = EditorFactory.getInstance();
        if (editor != null) {
            editorFactory.releaseEditor(editor);
        }
        String fileExt = FileUtilRt.getExtension(template.getName());
        FileType fileType =  FileTypeManager.getInstance().getFileTypeByExtension(fileExt);

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

        // 添加修改事件
        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void beforeDocumentChange(DocumentEvent event) {

            }

            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                String text = editor.getDocument().getText();
                // 回调事件
                if (callback != null && !Objects.equals(text, template.getCode())) {
                    callback.call();
                }
            }
        });

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

        // panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(editor.getComponent(), BorderLayout.CENTER);

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

    /**
     * 回调接口
     */
    public interface Callback {
        /**
         * 文档修改回调
         */
        void call();
    }

    public TigenTemplate getTemplate() {
        return template;
    }

    public void setTemplate(TigenTemplate template) {
        this.template = template;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
