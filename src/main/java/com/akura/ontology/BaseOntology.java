package com.akura.ontology;

import com.akura.config.Config;
import com.akura.utility.OntologyReader;
import org.apache.jena.ontology.OntModel;

public class BaseOntology {

    private static OntModel m = OntologyReader.getOntologyModel(Config.OWL_FILENAME);

    public static OntModel getInstance(){
        return m;
    }
}
