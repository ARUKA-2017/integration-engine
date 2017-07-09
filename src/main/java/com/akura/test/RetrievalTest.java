package com.akura.test;


import com.akura.config.Config;
import com.akura.retrieval.response.SingleResponse;
import com.akura.utility.OntologyReader;
import org.apache.jena.ontology.OntModel;

public class RetrievalTest {
    public static void main(String[] args) {

        OntModel m = OntologyReader.getOntologyModel(Config.OWL_FILENAME);
//
//        Entity entity = new Entity(m);
//        entity.getEntityByHash(HashGeneratorClass.generateHashForString("Samsung Galaxy S5", "ENTITY"));
//
//            System.out.println(entity.instance );
//        if (entity.instance != null) {
//            // create response class instance
//
//            SingleResponse resp = new SingleResponse();
//
//            resp.name = entity.getName();
//            resp.id = entity.getHash();
//            resp.avg_baseScore = entity.getAvgBaseScore();
//           // resp.features =   entity.getFeatures();
//
//            System.out.println(resp.avg_baseScore );
//
//        }

//        SingleResponse resp = new SingleResponse(m,"Samsung Galaxy S5");



//        Comparison comparison = new Comparison(m);
//        System.out.println(comparison.getBetterThanForEntity("ENTITY--1572451790"));


        SingleResponse resp = new SingleResponse(m, "iphone7", false);
        System.out.println(resp);

        System.out.println(resp.toString());
        System.out.println(resp.features[0].comparisons.betterThan[0].id);
        System.out.println(resp.features[0].comparisons.betterThan[0].count);
    }
}
