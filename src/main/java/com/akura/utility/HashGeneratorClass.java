package com.akura.utility;

import java.util.Date;

/**
 * Class representing a HashGeneratorClass.
 */
public class HashGeneratorClass {

    /**
     * Method used to generate hash id for the given value.
     *
     * @param value  - value to be hashed.
     * @param prefix - prefix to be inserted.
     * @return - hash id with prefix.
     */
    public static String generateHashForString(String value, String prefix) {

        return prefix + "-" + value
                .toUpperCase()
                .replaceAll("[^a-zA-Z0-9]+", "")
                .trim()
                .hashCode();
    }

    /**
     * Method used to generate hash from timestamp.
     *
     * @param prefix - prefix to be inserted.
     * @return - hash id with prefix.
     */
    public static String generateFromTimeStamp(String prefix) {

        return prefix + "-" + new Date().getTime() + "" + (int) Math.ceil(Math.random() * 10);
    }

}
