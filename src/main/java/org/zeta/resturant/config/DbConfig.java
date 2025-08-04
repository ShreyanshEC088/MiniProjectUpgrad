package org.zeta.resturant.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbConfig {

    private static final String PROPERTIES_FILE = "application.properties";
    private static Properties properties = new Properties();

    static {
        try (InputStream is = DbConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (is == null) {
                throw new RuntimeException("application.properties not found in classpath");
            }
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load database properties", e);
        }
    }

    public static String getUrl() {
        return properties.getProperty("db.url");
    }

    public static String getUser() {
        return properties.getProperty("db.user");
    }

    public static String getPassword() {
        return properties.getProperty("db.password");
    }
}