package com.github.czy211.ij.template;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;
import org.jetbrains.annotations.Nullable;

public class UserLiveTemplateProvider implements DefaultLiveTemplatesProvider {
  @Override
  public String[] getDefaultLiveTemplateFiles() {
    return new String[]{"/liveTemplates/User.xml"};
  }

  @Nullable
  @Override
  public String[] getHiddenLiveTemplateFiles() {
    return null;
  }
}
