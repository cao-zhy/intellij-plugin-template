package com.github.czy211.ij.template;

import com.intellij.ide.IdeView;
import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.ide.actions.CreateFileAction;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.ide.fileTemplates.actions.CreateFromTemplateAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class CreateHtmlFileDialog extends DialogWrapper {
  private final IdeView view;
  private PsiDirectory dir;
  private JBTextField nameText;
  private JBTextField langText;
  private JBCheckBox thymeleafCheck;
  private JBCheckBox jqueryCheck;
  private JComboBox<String> bootstrapCombo;
  private JBCheckBox bootstrapCssCheck;
  private JBCheckBox bootstrapJsCheck;

  public CreateHtmlFileDialog(IdeView view, PsiDirectory dir) {
    super(true);
    this.view = view;
    this.dir = dir;
    setTitle("New HTML File");
    init();
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    JPanel centerPanel = new JPanel(new VerticalFlowLayout());

    nameText = new JBTextField();

    JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    langText = new JBTextField("zh");
    thymeleafCheck = new JBCheckBox("Thymeleaf");
    jqueryCheck = new JBCheckBox("JQuery");
    panel1.add(new JBLabel("lang:"));
    panel1.add(langText);
    panel1.add(thymeleafCheck);
    panel1.add(jqueryCheck);

    JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    bootstrapCombo = new ComboBox<>(new String[]{"", "v5", "v4", "v3"});
    bootstrapCssCheck = new JBCheckBox("CSS");
    bootstrapJsCheck = new JBCheckBox("JS");
    panel2.add(new JBLabel("Bootstrap:"));
    panel2.add(bootstrapCombo);
    panel2.add(bootstrapCssCheck);
    panel2.add(bootstrapJsCheck);

    centerPanel.add(nameText);
    centerPanel.add(panel1);
    centerPanel.add(panel2);

    bootstrapCssCheck.setEnabled(false);
    bootstrapJsCheck.setEnabled(false);
    bootstrapCombo.addItemListener(e -> {
      boolean enabled = !"".equals(e.getItem());
      bootstrapCssCheck.setEnabled(enabled);
      bootstrapJsCheck.setEnabled(enabled);
    });

    return centerPanel;
  }

  @Override
  protected void doOKAction() {
    // 处理输入的文件名
    String name = nameText.getText().trim();
    CreateFileAction.MkDirs mkDirs = new CreateFileAction.MkDirs(name, dir);
    name = mkDirs.newName;
    dir = mkDirs.directory;

    FileTemplate template = FileTemplateManager.getDefaultInstance().getInternalTemplate("HTML");
    Properties properties = new Properties();
    properties.setProperty("LANG", langText.getText());
    properties.setProperty("THYMELEAF", String.valueOf(thymeleafCheck.isSelected()));
    properties.setProperty("JQUERY", String.valueOf(jqueryCheck.isSelected()));
    properties.setProperty("BOOTSTRAP", String.valueOf(bootstrapCombo.getSelectedItem()));
    properties.setProperty("BOOTSTRAP_CSS", String.valueOf(bootstrapCssCheck.isSelected()));
    properties.setProperty("BOOTSTRAP_JS", String.valueOf(bootstrapJsCheck.isSelected()));
    PsiFile psiFile = null;
    try {
      psiFile = FileTemplateUtil.createFromTemplate(template, name, properties, dir).getContainingFile();
      // 启用实时模板并打开文件
      CreateFromTemplateAction.startLiveTemplate(psiFile);
      // 移动光标至实时模板变量位置
      Editor editor = FileEditorManager.getInstance(dir.getProject()).getSelectedTextEditor();
      int offset = getOffsetToPreserve(editor);
      view.selectElement(psiFile);
      if (offset != -1 && editor != null && !editor.isDisposed()) {
        editor.getCaretModel().moveToOffset(offset);
      }
    } catch (Exception e) {
      Messages.showErrorDialog(nameText, CreateElementActionBase.filterMessage(e.getMessage()));
      nameText.requestFocus(true);
    }
    if (psiFile != null) {
      super.doOKAction();
    }
  }

  @Nullable
  @Override
  protected ValidationInfo doValidate() {
    if (StringUtil.isEmpty(nameText.getText().trim())) {
      return new ValidationInfo("Name can't be empty", nameText);
    }
    return super.doValidate();
  }

  @Nullable
  @Override
  public JComponent getPreferredFocusedComponent() {
    return nameText;
  }

  private static Integer getOffsetToPreserve(Editor editor) {
    if (editor == null) return -1;
    int offset = editor.getCaretModel().getOffset();
    if (offset == 0) return -1;
    return offset;
  }
}
