package com.akura.retrieval.service;


import com.akura.config.Config;
import com.akura.retrieval.models.Entity;
import com.akura.retrieval.models.response.SingleResponse;
import com.akura.utility.HashGeneratorClass;
import com.akura.utility.OntologyReader;
import org.apache.jena.ontology.OntModel;
import spark.Response;

public class ProductService {

    OntModel m = OntologyReader.getOntologyModel(Config.OWL_FILENAME);

    public SingleResponse searchProduct(String search, Response res) {

        Entity entity = new Entity(m);
        entity.getEntityByHash(HashGeneratorClass.generateHashForString(search, "ENTITY"));
        System.out.println(entity.instance);
        SingleResponse resp = new SingleResponse();
        if (entity.instance != null) {
            // create response class instance


            resp.name = entity.getName();
            resp.id = entity.getHash();
            resp.avg_baseScore = entity.getAvgBaseScore();
            resp.features = entity.getFeatures();

        } else {
            // TODO: Search from name string
        }

        res.type("Application/JSON");
        return resp;
    }
}
