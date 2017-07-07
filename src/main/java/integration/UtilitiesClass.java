package integration;


import org.apache.jena.ontology.OntModel;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class UtilitiesClass {

    public static String fileName = "rev_engine_base_ontology.owl";
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
