package com.akura.utility;


import org.apache.jena.ontology.OntModel;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class OntologyWriter {

    // File manager
    public static FileResourceManager fileResourceManager = new FileResourceManager();

    public static String fileName = fileResourceManager.getFilePath("ontology/rev_engine_base_ontology.owl");
    public static String fileType = "RDF/XML-ABBREV";

    public static void writeOntology(OntModel m){

        try{
            PrintStream p = new PrintStream(fileName);
            m.write(p, fileType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
