package com.github.czy211.ij.template;

import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.ide.actions.OpenFileAction;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class InitModuleDialog extends DialogWrapper {
  private final PsiDirectory dir;
  private JBCheckBox readmeCheck;
  private JBCheckBox gitignoreCheck;
  private JComboBox<String> licenseCombo;

  public InitModuleDialog(@NotNull PsiDirectory dir) {
    super(true);
    this.dir = dir;
    setTitle("Initialize Module");
    init();
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    JPanel centerPanel = new JPanel(new VerticalFlowLayout());

    readmeCheck = new JBCheckBox("Add a README.md file");
    gitignoreCheck = new JBCheckBox("Add .gitignore");

    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    int length = Constant.LICENSE_TEMPLATES.length;
    String[] licenses = new String[length + 1];
    licenses[0] = "";
    System.arraycopy(Constant.LICENSE_TEMPLATES, 0, licenses, 1, length);
    licenseCombo = new ComboBox<>(licenses);
    panel.add(new JBLabel("Choose a license:"));
    panel.add(licenseCombo);

    centerPanel.add(readmeCheck);
    centerPanel.add(gitignoreCheck);
    centerPanel.add(panel);

    String path = dir.getVirtualFile().getPath();
    boolean readmeExists = new File(path, "README.md").exists();
    boolean gitignoreExists = new File(path, ".gitignore").exists();
    boolean licenseExists = new File(path, "LICENSE.txt").exists();
    readmeCheck.setEnabled(!readmeExists);
    readmeCheck.setSelected(!readmeExists);
    gitignoreCheck.setEnabled(!gitignoreExists);
    gitignoreCheck.setSelected(!gitignoreExists);
    licenseCombo.setEnabled(!licenseExists);
    licenseCombo.setSelectedItem(licenseExists ? "" : "MIT");

    return centerPanel;
  }

  @Override
  protected void doOKAction() {
    StringBuilder errorMessage = new StringBuilder();
    PsiFile readme = null;
    if (readmeCheck.isSelected()) {
      try {
        readme = WriteAction.compute(() -> dir.createFile("README.md"));
      } catch (Exception e) {
        errorMessage.append(CreateElementActionBase.filterMessage(e.getMessage())).append("\n");
      }
    }
    PsiFile gitignore = null;
    if (gitignoreCheck.isSelected()) {
      try {
        gitignore = WriteAction.compute(() -> dir.createFile(".gitignore"));
      } catch (Exception e) {
        errorMessage.append(CreateElementActionBase.filterMessage(e.getMessage())).append("\n");
      }
    }
    PsiFile license = null;
    if (!"".equals(licenseCombo.getSelectedItem())) {
      FileTemplate template = FileTemplateManager.getDefaultInstance().getJ2eeTemplate(String.valueOf(licenseCombo
        .getSelectedItem()));
      try {
        license = FileTemplateUtil.createFromTemplate(template, "LICENSE", null, dir).getContainingFile();
      } catch (Exception e) {
        errorMessage.append(CreateElementActionBase.filterMessage(e.getMessage())).append("\n");
      }
    }
    if (!"".contentEquals(errorMessage)) {
      Messages.showErrorDialog(getContentPanel(), errorMessage.toString());
    } else {
      Project project = dir.getProject();
      if (readme != null) {
        OpenFileAction.openFile(readme.getVirtualFile(), project);
      }
      if (gitignore != null) {
        OpenFileAction.openFile(gitignore.getVirtualFile(), project);
      }
      if (license != null) {
        OpenFileAction.openFile(license.getVirtualFile(), project);
      }
      super.doOKAction();
    }
  }

  @Nullable
  @Override
  public JComponent getPreferredFocusedComponent() {
    return licenseCombo;
  }
}
