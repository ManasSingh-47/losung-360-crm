package com.crm.contacts.config;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static String hibernateURL;
    public static String hibernateUsername;
    public static String hibernatePassword;
    public static String hibernateDriverClass;
    public static String hibernateDialect;

    static {
        Properties settings = new Properties();
        try {
            InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("application.properties");
            settings.load(inputStream);

            hibernateDriverClass = settings.getProperty("spring.datasource.driver-class-name");
            hibernateURL = settings.getProperty("spring.datasource.url");
            hibernateUsername = settings.getProperty("spring.datasource.username");
            hibernatePassword = settings.getProperty("spring.datasource.password");
            hibernateDialect = settings.getProperty("hibernate.dialect");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
