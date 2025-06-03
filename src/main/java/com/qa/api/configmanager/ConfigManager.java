package com.qa.api.configmanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static Properties properties = new Properties();

    static {
      InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream("config/config.properties");
      if (inputStream != null) {
          try {
              properties.load(inputStream);
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key).trim();
    }
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

}
