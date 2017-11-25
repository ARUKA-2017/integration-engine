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

    OntModel m = OntologyReader.getOntologyModel(Config.OWL_FILENAME);

    public IRetrievalResponse searchProduct(String search, Response res, boolean isHash) {

        res.type("Application/JSON");

        if (InstanceChecker.isEntityExists(m, search, isHash)) {
            return new SingleResponse(m, search, isHash);
        } else {

            BackgroundService backgroundService = new BackgroundService(search);
            backgroundService.start();

            return new EntityListResponse(m, search);
        }
    }
}
