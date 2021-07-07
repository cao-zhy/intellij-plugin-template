package com.github.czy211.ij.template;

import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;

public class Util {
  public static PsiDirectory getOrChooseDirectory(AnActionEvent anActionEvent) {
    IdeView view = LangDataKeys.IDE_VIEW.getData(anActionEvent.getDataContext());
    if (view != null) {
      Project project = anActionEvent.getProject();
      PsiDirectory dir = view.getOrChooseDirectory();
      if (project != null && dir != null) {
        return dir;
      }
    }
    return null;
  }
}
