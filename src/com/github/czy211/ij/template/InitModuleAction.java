package com.github.czy211.ij.template;

import com.intellij.ide.util.DirectoryChooserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;

import java.util.HashSet;
import java.util.Set;

public class InitModuleAction extends AnAction {
  public InitModuleAction() {
    super("Initialize Module", "Create README.md, .gitignore, LICENSE.txt files", null);
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    // 未选择文件和文件夹或选择了不同文件夹的多个文件时，也可以初始化模块，创建自定义方法获取创建创建的位置
    PsiDirectory dir = getOrChooseDirectory(e);
    if (dir != null) {
      new InitModuleDialog(dir).show();
    }
  }

  @Override
  public void update(AnActionEvent e) {
    // IdeView 不存在时也可以初始化模块
    e.getPresentation().setEnabledAndVisible(e.getProject() != null);
  }

  private PsiDirectory getOrChooseDirectory(AnActionEvent e) {
    Project project = e.getProject();
    if (project == null) {
      return null;
    }
    // 默认值为项目根文件夹
    PsiDirectory dir = PsiManager.getInstance(project).findDirectory(project.getBaseDir());
    // 在项目窗口已选择的文件或文件夹
    VirtualFile[] selectedFiles = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(e.getDataContext());
    if (selectedFiles != null) {
      // 已选择模块的根文件夹
      Set<PsiDirectory> selectedDirs = new HashSet<>();
      for (VirtualFile virtualFile : selectedFiles) {
        Module module = ModuleUtil.findModuleForFile(virtualFile, project);
        if (module != null) {
          VirtualFile[] contentRoots = ModuleRootManager.getInstance(module).getContentRoots();
          PsiDirectory directory = PsiManager.getInstance(project).findDirectory(contentRoots[0]);
          selectedDirs.add(directory);
        }
      }
      // 文件创建的位置
      PsiDirectory selectedDir = getOrChooseDirectory(selectedDirs.toArray(new PsiDirectory[0]));
      if (selectedDir != null) {
        dir = selectedDir;
      } else if (selectedDirs.size() > 0) { // 未在弹出的文件夹选择框选择文件夹
        return null;
      }
    }
    return dir;
  }

  /**
   * 获得文件创建的位置
   *
   * @param dirs 在项目窗口中已选择文件所属模块的根文件夹
   * @return 文件创建的位置
   */
  private PsiDirectory getOrChooseDirectory(PsiDirectory[] dirs) {
    if (dirs.length == 0) {
      return null;
    }
    if (dirs.length == 1) {
      return dirs[0];
    }
    Project project = dirs[0].getProject();
    return DirectoryChooserUtil.selectDirectory(project, dirs, null, "");
  }
}
