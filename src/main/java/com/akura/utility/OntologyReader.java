package com.akura.utility;


import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.jena.ontology.OntModelSpec.OWL_MEM;

/**
 * Class that is used to read the ontology
 */
public class OntologyReader {

    public static String fileName = "ontology/rev_engine_base_ontology.owl";
    public static String fileType = "RDF/XML-ABBREV";

    // File manager
    public static FileResourceManager fileResourceManager = new FileResourceManager();

    public static OntModel getOntologyModel(String file)
    {
        InputStream in = null;
        OntModel model = ModelFactory.createOntologyModel(OWL_MEM);

        try
        {
            in = new FileInputStream(fileResourceManager.getFilePath(file));
            model.read(in, fileType);
            in.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return model;
    }
}
