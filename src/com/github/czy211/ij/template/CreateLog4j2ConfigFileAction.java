package com.github.czy211.ij.template;

import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;

public class CreateLog4j2ConfigFileAction extends AnAction {
  public CreateLog4j2ConfigFileAction() {
    super("Log4j2 Config", "Create New Log4j2 Config File", AllIcons.FileTypes.Xml);
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    PsiDirectory dir = Util.getOrChooseDirectory(e);
    if (dir == null) {
      return;
    }
    Project project = dir.getProject();
    FileTemplate template = FileTemplateManager.getDefaultInstance().getInternalTemplate("Log4j2 Config");
    try {
      if (dir.getVirtualFile().findChild("Log4j-config.xsd") == null) {
        FileTemplate xsdTemplate = FileTemplateManager.getDefaultInstance().findInternalTemplate("Log4j2 Config Xsd");
        // 首次在该文件夹创建时，Log4j-config.xsd 会使用设置中 XML 文件的 CodeStyle 进行格式化
        PsiFile psiFile = CreateFileFromTemplateAction.createFileFromTemplate("Log4j-config", xsdTemplate, dir, null,
          false);
        // 再次格式化，才会使用 EditorConfig 配置
        if (psiFile != null) {
          WriteAction.compute(() -> CodeStyleManager.getInstance(project).reformat(psiFile));
        }
      }
      CreateFileFromTemplateAction.createFileFromTemplate("log4j2", template, dir, null, true);
    } catch (Exception ex) {
      Messages.showErrorDialog(project, CreateElementActionBase.filterMessage(ex.getMessage()), "Error");
    }
  }
}
