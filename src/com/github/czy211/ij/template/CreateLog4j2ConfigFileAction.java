package com.github.czy211.ij.template;

import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;

public class CreateLog4j2ConfigFileAction extends AnAction {
  public CreateLog4j2ConfigFileAction() {
    super("Log4j2 Config", "Create New Log4j2 Config File", AllIcons.FileTypes.Xml);
    FileTemplate template = FileTemplateManager.getDefaultInstance().getInternalTemplate("Log4j2 Config Xsd");
    template.setReformatCode(false);
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
        FileTemplate xsdTemplate = FileTemplateManager.getDefaultInstance().getInternalTemplate("Log4j2 Config Xsd");
        CreateFileFromTemplateAction.createFileFromTemplate("Log4j-config", xsdTemplate, dir, null, false);
      }
      CreateFileFromTemplateAction.createFileFromTemplate("log4j2", template, dir, null, true);
    } catch (Exception ex) {
      Messages.showErrorDialog(project, CreateElementActionBase.filterMessage(ex.getMessage()), "Error");
    }
  }
}
