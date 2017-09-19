package com.akura.test;


import com.akura.config.Config;
import com.akura.retrieval.response.SingleResponse;
import com.akura.utility.OntologyReader;
import org.apache.jena.ontology.OntModel;

public class RetrievalTest {
    public static void main(String[] args) {

        OntModel m = OntologyReader.getOntologyModel(Config.OWL_FILENAME);
        SingleResponse resp = new SingleResponse(m, "Samsung Galaxy S5", false);

    }
}
