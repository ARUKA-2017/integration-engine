package com.akura.parser.vowl;

import com.akura.utility.CommandExecutor;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class VowlConverter {

    public static void convert(){

        String domainName = "java -jar " + System.getProperty("user.dir") + "\\lib\\owl2vowl.jar " +
                "-file " + System.getProperty("user.dir") + "\\test-1.owl  " +
                "-output " + System.getProperty("user.dir") + "\\webvowl\\data\\foaf.json  ";

        System.out.println(domainName);
        String output = CommandExecutor.executeCommand(domainName);
        System.out.println(output);
    }

}
