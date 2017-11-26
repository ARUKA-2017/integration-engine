package com.akura.utility;

import com.akura.config.Config;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.jena.ontology.OntModelSpec.OWL_MEM;

/**
 * Class representing an OntologyReader.
 */
public class OntologyReader {

    public static OntModel staticModel;

    static {
        InputStream in = null;
        staticModel = ModelFactory.createOntologyModel(OWL_MEM);

        try {
            in = new FileInputStream(Config.OWL_FILENAME);
            staticModel.read(in, "RDF/XML");
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String fileType = "RDF/XML-ABBREV";

    public static FileResourceManager fileResourceManager = new FileResourceManager();

    /**
     * Method used to get the ontology model.
     *
     * @param file -  name of the file.
     * @return - Ontology model.
     */
    public static OntModel getOntologyModel(String file) {

        if (file.equals(Config.OWL_FILENAME)) {
            return staticModel;
        }

        InputStream in = null;
        OntModel model = ModelFactory.createOntologyModel(OWL_MEM);

        try {
            in = new FileInputStream(file);
            model.read(in, "RDF/XML");
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return model;
    }
}
