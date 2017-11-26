package com.akura.retrieval.service;

import com.akura.config.Config;
import com.akura.mapping.service.MappingService;
import com.akura.ontology.BaseOntology;
import com.akura.retrieval.response.EntityListResponse;
import com.akura.retrieval.response.IRetrievalResponse;
import com.akura.retrieval.response.SingleResponse;
import com.akura.utility.InstanceChecker;
import com.akura.utility.OntologyReader;

import org.apache.jena.ontology.OntModel;

import spark.Response;

/**
 * Class representing a ProductService.
 */
public class ProductService {

    OntModel m = BaseOntology.getInstance();

    /**
     * Method used to search the product.
     *
     * @param search         - search keyword.
     * @param res            - response of the request.
     * @param isHash         - is it a hash based entity search.
     * @param mappingService - instance of Mapping Service.
     * @return - IRetrievalResponse.
     */
    public IRetrievalResponse searchProduct(String search, Response res, boolean isHash, MappingService mappingService) {

        res.type("Application/JSON");

        if (InstanceChecker.isEntityExists(m, search, isHash)) {
            return new SingleResponse(m, search, isHash);
        } else {

            BackgroundService backgroundService = new BackgroundService(search, mappingService, res);
            backgroundService.start();

            return new EntityListResponse(m, search);
        }
    }
}
