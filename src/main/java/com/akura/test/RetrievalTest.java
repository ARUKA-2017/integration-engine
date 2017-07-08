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

        Entity entity = new Entity(m);
        entity.getEntityByHash(HashGeneratorClass.generateHashForString("iphone 7", "ENTITY"));

        if (entity.instance != null) {
            // create response class instance

            SingleResponse resp = new SingleResponse();

            resp.name = entity.getName();
            resp.id = entity.getHash();
            resp.avg_baseScore = entity.getAvgBaseScore();
            resp.features =   entity.getFeatures();


        }
    }
}
