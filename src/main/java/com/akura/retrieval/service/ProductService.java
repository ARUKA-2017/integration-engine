package com.akura.retrieval.service;


import com.akura.config.Config;
import com.akura.retrieval.response.EntityListResponse;
import com.akura.retrieval.response.IRetrievalResponse;
import com.akura.retrieval.response.SingleResponse;
import com.akura.utility.InstanceChecker;
import com.akura.utility.OntologyReader;
import org.apache.jena.ontology.OntModel;
import spark.Response;

public class ProductService {

    //TODO Implement a singleton for Ontology Model
    OntModel m = OntologyReader.getOntologyModel(Config.OWL_FILENAME);

    public IRetrievalResponse searchProduct(String search, Response res, boolean isHash) {

        res.type("Application/JSON");

        if(InstanceChecker.isEntityExists(m, search, isHash)) {
            SingleResponse resp = new SingleResponse(m, search, isHash);
            return resp;
        } else {
            EntityListResponse response = new EntityListResponse(m, search);
            return  response;
        }
    }
}
