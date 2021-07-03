package com.github.czy211.ij.template;

import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;

public class LicenseTemplateGroupDescriptorFactory implements FileTemplateGroupDescriptorFactory {
  @Override
  public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
    FileTemplateGroupDescriptor group = new FileTemplateGroupDescriptor("LICENSE", AllIcons.FileTypes.Text);
    for (String name : Constant.LICENSE_TEMPLATES) {
      group.addTemplate(name + ".txt");
    }
    return group;
  }
}
