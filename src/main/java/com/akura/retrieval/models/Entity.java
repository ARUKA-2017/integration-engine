package com.akura.retrieval.models;

import com.akura.config.Config;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;

public class Entity {

    private OntModel model;
    public Individual instance;

    Entity(OntModel m) {
        this.model = m;
    }

    public Individual getEntityByHash(String hash) {
        Individual ind = this.model.getIndividual(Config.URI_NAMESPACE + hash);
        this.instance = ind;
        return ind;
    }


    public void searchEntityByString(String name) {

        //TODO
    }
}
