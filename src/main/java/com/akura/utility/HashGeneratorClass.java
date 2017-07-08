package com.akura.utility;


import java.security.Timestamp;
import java.util.Date;

public class HashGeneratorClass {

    public static String generateHashForString(String value, String prefix) {

        return prefix +"-" +value
                .toUpperCase()
                .replaceAll("[^a-zA-Z0-9]+", "")
                .trim()
                .hashCode();
    }

    public static  String generateFromTimeStamp(String prefix) {

        return prefix +"-"+ new Date().getTime() + "" + (int) Math.ceil(Math.random()*10);
    }

}
