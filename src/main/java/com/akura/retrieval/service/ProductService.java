package com.akura.retrieval.service;


import com.akura.config.Config;
import com.akura.retrieval.models.Entity;
import com.akura.retrieval.response.SingleResponse;
import com.akura.utility.HashGeneratorClass;
import com.akura.utility.OntologyReader;
import org.apache.jena.ontology.OntModel;
import spark.Response;

public class ProductService {

    //TODO Implement a singleton for Ontology Model
    OntModel m = OntologyReader.getOntologyModel(Config.OWL_FILENAME);

    public SingleResponse searchProduct(String search, Response res) {

        SingleResponse resp = new SingleResponse(m, search);
        res.type("Application/JSON");
        return resp;
    }
}
