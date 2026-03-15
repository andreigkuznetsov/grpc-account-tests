package account.config;

import java.io.InputStream;
import java.util.Properties;

public final class PropertiesLoader {

    private static final String PROPERTIES_FILE = "application.properties";
    private static final Properties PROPERTIES = loadProperties();

    private PropertiesLoader() {
    }

    private static Properties loadProperties() {
        try (InputStream inputStream = PropertiesLoader.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (inputStream == null) {
                throw new IllegalStateException("File not found in classpath: " + PROPERTIES_FILE);
            }

            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load properties from " + PROPERTIES_FILE, e);
        }
    }

    public static String getRequired(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Required property is missing or blank: " + key);
        }
        return value;
    }

    public static String get(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        String value = PROPERTIES.getProperty(key);
        return value == null || value.isBlank() ? defaultValue : Integer.parseInt(value);
    }

    public static long getLong(String key, long defaultValue) {
        String value = PROPERTIES.getProperty(key);
        return value == null || value.isBlank() ? defaultValue : Long.parseLong(value);
    }
}