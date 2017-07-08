package com.akura.test;


import com.akura.config.Config;
import com.akura.retrieval.models.Entity;
import com.akura.retrieval.models.response.FeatureResponse;
import com.akura.retrieval.models.response.SingleResponse;
import com.akura.utility.HashGeneratorClass;
import com.akura.utility.OntologyReader;
import org.apache.jena.ontology.OntModel;

import java.util.ArrayList;

public class RetrievalTest {
    public static void main(String[] args) {

        OntModel m = OntologyReader.getOntologyModel(Config.OWL_FILENAME);

        SingleResponse resp = new SingleResponse(m,"Samsung Galaxy S5");



    }
}
