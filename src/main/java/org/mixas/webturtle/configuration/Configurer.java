package org.mixas.webturtle.configuration;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Mikhail Stryzhonok
 */
public class Configurer {
    private static final Logger LOGGER = Logger.getLogger(Configurer.class);

    private static final String CONFIG_FILE_NAME = "application.properties";
    private static final String CONFIG_RESOURCE = "org/mixas/webturtle/configuration/" + CONFIG_FILE_NAME;
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
            LOGGER.debug("Try to load external configuration");
            prop.load(new FileInputStream(EXTERNAL_CONFIG_PATH));
        } catch (IOException ex) {
            try {
                LOGGER.debug("External configuration failed to load. Now internal configuration will be loaded");
                getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
                prop.load(getClass().getClassLoader().getResourceAsStream(CONFIG_RESOURCE));
            } catch (IOException e) {
                LOGGER.debug("Failed to load internal configuration", e);
                throw new IllegalStateException("No external or internal config files found. Seems like jar archive corrupted");
            }
        }
    }

    public String getStringProperty(String key) throws MissingPropertyException {
        String property = prop.getProperty(key);
        if (property == null) {
            LOGGER.debug("Missing propetry" + key);
            throw new MissingPropertyException("Not found string property for key " + key);
        }
        return property;
    }

    public int getIntProperty(String key) throws MissingPropertyException{
        return Integer.parseInt(getStringProperty(key));
    }
}
