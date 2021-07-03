package com.github.czy211.ij.template;

import com.google.gson.*;
import com.intellij.openapi.util.io.FileUtil;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CreateLicenseTemplatesFromGithub {
  public static void main(String[] args) {
    Map<String, String> templateMap = getTemplateMap();
    for (Map.Entry<String, String> entry : templateMap.entrySet()) {
      createLicenseTemplate(entry.getKey(), entry.getValue());
    }
  }

  private static void createLicenseTemplate(String name, String url) {
    JsonObject jsonObject = getJsonElementFromUrl(url).getAsJsonObject();
    String body = jsonObject.get("body").getAsString();
    URL classUrl = CreateLicenseTemplatesFromGithub.class.getResource("");
    if (classUrl != null) {
      String path = classUrl.getPath();
      String projectPath = path.substring(0, path.indexOf("/out/"));
      File file = new File(projectPath + "/resources/fileTemplates/j2ee/" + name + ".txt.ft");
      try {
        FileUtil.writeToFile(file, body);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private static Map<String, String> getTemplateMap() {
    Map<String, String> map = new HashMap<>();
    JsonArray array = getJsonElementFromUrl("https://api.github.com/licenses").getAsJsonArray();
    for (JsonElement element : array) {
      JsonObject jsonObject = element.getAsJsonObject();
      String name = jsonObject.get("spdx_id").getAsString();
      String url = jsonObject.get("url").getAsString();
      map.put(name, url);
    }
    return map;
  }

  private static JsonElement getJsonElementFromUrl(String url) {
    try {
      HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
      Reader reader = new InputStreamReader(connection.getInputStream());
      return new JsonParser().parse(reader);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return JsonNull.INSTANCE;
  }
}
