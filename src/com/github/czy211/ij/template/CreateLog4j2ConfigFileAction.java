package com.github.czy211.ij.template;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeView;
import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;

public class CreateLog4j2ConfigFileAction extends AnAction {
  public CreateLog4j2ConfigFileAction() {
    super("Log4j2 Config", "Create New Log4j2 Config File", AllIcons.FileTypes.Xml);
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    DataContext dataContext = e.getDataContext();
    IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
    if (view == null) {
      return;
    }
    Project project = e.getProject();
    PsiDirectory dir = view.getOrChooseDirectory();
    if (project == null || dir == null) {
      return;
    }
    FileTemplate xsdTemplate = FileTemplateManager.getDefaultInstance().findInternalTemplate("Log4j2 Config Xsd");
    FileTemplate template = FileTemplateManager.getDefaultInstance().getInternalTemplate("Log4j2 Config");
    try {
      CreateFileFromTemplateAction.createFileFromTemplate("Log4j-config", xsdTemplate, dir, null, false);
      CreateFileFromTemplateAction.createFileFromTemplate("log4j2", template, dir, null, true);
    } catch (Exception ex) {
      Messages.showErrorDialog(project, CreateElementActionBase.filterMessage(ex.getMessage()), "Error");
    }
  }
}
