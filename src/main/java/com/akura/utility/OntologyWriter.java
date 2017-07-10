package com.akura.utility;


import com.akura.config.Config;
import org.apache.jena.ontology.OntModel;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class OntologyWriter {

    // File manager
    public static FileResourceManager fileResourceManager = new FileResourceManager();

    public static String fileName = Config.OWL_FILENAME;
    public static String fileType = "RDF/XML-ABBREV";

    public static void writeOntology(OntModel m){

        try{
            PrintStream p = new PrintStream(fileName);
            m.write(p, fileType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // TODO save these files in a seperate folder organized by date
    public static void writeOntology(OntModel m, String fileName){

        try{
            PrintStream p = new PrintStream(fileName);
            m.write(p, fileType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
