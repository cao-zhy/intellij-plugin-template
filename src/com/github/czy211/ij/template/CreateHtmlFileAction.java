package com.github.czy211.ij.template;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;

public class CreateHtmlFileAction extends AnAction {
  public CreateHtmlFileAction() {
    super("HTML File", "Create new HTML file", AllIcons.FileTypes.Html);
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
    new CreateHtmlFileDialog(view, dir).show();
  }
}
