package com.github.czy211.ij.template;

import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;

public class TemplatesGroup extends DefaultActionGroup {
  public TemplatesGroup() {
    super("Templates", true);
  }

  @Override
  public void update(AnActionEvent e) {
    Project project = e.getProject();
    IdeView view = LangDataKeys.IDE_VIEW.getData(e.getDataContext());
    boolean enabled = project != null && view != null && view.getDirectories().length != 0;
    Presentation presentation = e.getPresentation();
    presentation.setEnabledAndVisible(enabled);
  }
}
