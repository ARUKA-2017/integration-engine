package com.akura.logger;

import org.apache.log4j.PropertyConfigurator;

import java.util.HashMap;

public class FileLogger {

    public static String DEST_J2OWL = "j2owl";
    public static String DEST_INTEGRATION = "integration";
    public static String DEST_RETRIEVAL = "integration";

    public static String DEST_ENTIRE_PROCESS_LOG = "System";

    public static String TYPE_TITLE = "#TITLE-";
    public static String TYPE_SUB = "#SUB-";
    public static String TYPE_CONT = "#CONT-";
    public static String TYPE_JSON = "#JSON-";
    public static String TYPE_PROCESS = "#PROCESS-";


    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getRootLogger();

    public static void Log(String s, String type, String destination) {
        PropertyConfigurator.configure("log4j-overall.properties");

        logger.info( type+destination+ "##$$$"+s);
        System.out.println(s);
    }

}

