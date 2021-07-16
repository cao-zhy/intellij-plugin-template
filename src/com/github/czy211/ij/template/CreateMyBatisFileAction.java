package com.github.czy211.ij.template;

import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiDirectory;

public class CreateMyBatisFileAction extends CreateFileFromTemplateAction {
  public CreateMyBatisFileAction() {
    super("MyBatis File", "Create new MyBatis file", IconLoader.getIcon("/icons/ibatis.svg"));
    FileTemplate configTemplate = FileTemplateManager.getDefaultInstance().findInternalTemplate("MyBatis Config");
    configTemplate.setReformatCode(false);
    FileTemplate mapperTemplate = FileTemplateManager.getDefaultInstance().findInternalTemplate("MyBatis Mapper");
    mapperTemplate.setReformatCode(false);
  }

  @Override
  protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
    builder.setTitle("New MyBatis File")
        .addKind("MyBatis Mapper file", AllIcons.FileTypes.Xml, "MyBatis Mapper")
        .addKind("MyBatis Config file", AllIcons.FileTypes.Xml, "MyBatis Config");
  }

  @Override
  protected String getActionName(PsiDirectory directory, String newName, String templateName) {
    return "Creating MyBatis file";
  }
}
