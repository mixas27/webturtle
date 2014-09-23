package org.mixas.webturtle.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Mikhail Stryzhonok
 */
public class Configurer {
    private static final String CONFIG_FILE_NAME = "application.properties";
    private static final String EXTERNAL_CONFIG_PATH = "./cfg/" + CONFIG_FILE_NAME;
    private static final Properties prop = new Properties();

    private static final Configurer INSTANCE = new Configurer();


    public static Configurer getInstance() {
        return INSTANCE;
    }

    private Configurer() {
        init();
    }

    private void init() {
        try {
            prop.load(new FileInputStream(EXTERNAL_CONFIG_PATH));
        } catch (IOException ex) {
            try {
                getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
                prop.load(getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME));
            } catch (IOException e) {
                throw new IllegalStateException("No external or internal config files found. Seems like jar archive corrupted");
            }
        }
    }

    public String getStringProperty(String key) throws MissingPropertyException {
        String property = prop.getProperty(key);
        if (property == null) {
            throw new MissingPropertyException("Not found string property for key " + key);
        }
        return property;
    }

    public int getIntProperty(String key) throws MissingPropertyException{
        return Integer.parseInt(getStringProperty(key));
    }
}
