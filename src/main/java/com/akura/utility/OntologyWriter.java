package com.akura.utility;

import com.akura.config.Config;

import org.apache.jena.ontology.OntModel;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Class representing an OntologyWriter.
 */
public class OntologyWriter {

    public static FileResourceManager fileResourceManager = new FileResourceManager();

    public static String fileName = Config.OWL_FILENAME;
    public static String fileType = "RDF/XML-ABBREV";

    /**
     * Method used to write the ontology.
     *
     * @param m - ontology model.
     */
    public static void writeOntology(OntModel m) {

        try {
            PrintStream p = new PrintStream(fileName);
            m.write(p, fileType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch( java.util.ConcurrentModificationException e){
            e.printStackTrace();
            System.out.println("Concurrency issue for saving ontology -->"+ fileName);
        }
    }

    /**
     * Method used to write ontology in separated with name.
     *
     * @param m        - Ontology name.
     * @param fileName - name of the file.
     */
    public static void writeOntology(OntModel m, String fileName) {

        try {
            PrintStream p = new PrintStream(fileName);
            m.write(p, fileType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
