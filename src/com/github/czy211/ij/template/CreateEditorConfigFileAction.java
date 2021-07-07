package com.github.czy211.ij.template;

import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;

public class CreateEditorConfigFileAction extends AnAction {
  public CreateEditorConfigFileAction() {
    super("EditorConfig File", "Create new EditorConfig file", AllIcons.FileTypes.Config);
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    PsiDirectory dir = Util.getOrChooseDirectory(e);
    if (dir == null) {
      return;
    }
    FileTemplate template = FileTemplateManager.getDefaultInstance().getInternalTemplate("EditorConfig");
    try {
      CreateFileFromTemplateAction.createFileFromTemplate("", template, dir, null, true);
    } catch (Exception ex) {
      Messages.showErrorDialog(e.getProject(), CreateElementActionBase.filterMessage(ex.getMessage()), "Error");
    }
  }
}
