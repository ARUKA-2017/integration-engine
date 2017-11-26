package com.akura.utility;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Class representing a Log.
 */
public class Log {
    final static Logger logger = Logger.getRootLogger();

    /**
     * Method used to write logs.
     *
     * @param message - log message.
     */
    public void write(String message) {
        PropertyConfigurator.configure("src/main/java/com/akura/config/log4j.properties");
        logger.info(message);
        // System.out.println(message);
    }
}
