package com.github.czy211.ij.template;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

public class CreateLog4j2ConfigXsdTemplateFromGitHub {
  public static void main(String[] args) {
    File file = new File("resources/fileTemplates/internal/Log4j2 Config Xsd.xsd.ft");
    try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
      HttpsURLConnection connection = (HttpsURLConnection) new URL("https://raw.githubusercontent.com/apache" 
        + "/logging-log4j2/master/log4j-core/src/main/resources/Log4j-config.xsd").openConnection();
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        writer.println(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
