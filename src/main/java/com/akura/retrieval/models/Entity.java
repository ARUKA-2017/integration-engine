package com.akura.retrieval.models;

import com.akura.config.Config;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

public class Entity {

    private OntModel model;
    public Individual instance;

    private Property name;
    private Property hashID;
    private ObjectProperty containProperty;
    private ObjectProperty feature;
    private ObjectProperty evaluatedBy;

    public Entity(OntModel m) {
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

    private void initProperties() {
        name = model.getProperty(Config.URI_NAMESPACE + "EntityName");
        hashID = model.getProperty(Config.URI_NAMESPACE + "HashID");
        containProperty = model.getObjectProperty(Config.URI_NAMESPACE + "ContainProperty");
        feature = model.getObjectProperty(Config.URI_NAMESPACE + "SubEntity");
        evaluatedBy = model.getObjectProperty(Config.URI_NAMESPACE + "EvaluatedBy");
    }

    public String getName() {
        return this.instance.getProperty(this.name).getLiteral().toString();
    }

    public String getHash() {
        return this.instance.getProperty(this.hashID).getLiteral().toString();
    }
}
