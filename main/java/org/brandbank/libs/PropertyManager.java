package org.brandbank.libs;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class PropertyManager {

    static Logger logger= Logger.getLogger(PropertyManager.class);

    public static Properties getPropertiesData() {
        String configFilePath = System.getProperty("user.dir")+"/config/config.properties";
        Properties properties = new Properties();
        try {
            logger.debug("Loading Properties file: "+configFilePath);
            properties.load(new FileInputStream(configFilePath));
        } catch (IOException e) {
            logger.debug("Unable to Load Properties file: "+configFilePath);
            throw new RuntimeException(e);
        }
        return properties;
    }
}
