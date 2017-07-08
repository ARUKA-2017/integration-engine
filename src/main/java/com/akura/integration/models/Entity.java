package com.akura.integration.models;


import com.akura.utility.HashGeneratorClass;
import com.akura.utility.OntologyWriter;
import org.apache.jena.ontology.*;
import com.akura.integration.OntologyClass;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;

import java.util.Map;


public class Entity {

    public static String prefix = "ENTITY";

    public Individual instance;
    public OntClass entityClass;

    private OntModel model;


    private Property name;
    private Property hashID;
    private ObjectProperty containProperty;
    private ObjectProperty feature;
    private ObjectProperty evaluatedBy;

    private String hash;

    public Entity(OntModel m, String name) {
        this.model = m;

        this.entityClass = (OntClass) this.model.getOntClass(OntologyClass.ENTITY);
        this.hash = HashGeneratorClass.generateHashForString(name, this.prefix);

        this.initProperties();

        Individual ind = this.search(this.hash);

        if (ind == null) {
            this.instance = entityClass.createIndividual(OntologyClass.URI_NAMESPACE
                    + this.hash);
            this.setEntityName(name);
            this.setHashID(this.hash);

        } else {
            this.instance = ind;
        }

        this.setEntityName(name);
        this.setHashID(this.hash);

    }


    public Entity(OntModel m) {
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(OntologyClass.ENTITY);
        this.initProperties();
    }


    private void initProperties() {
        name = model.getProperty(OntologyClass.URI_NAMESPACE + "EntityName");
        hashID = model.getProperty(OntologyClass.URI_NAMESPACE + "HashID");

        containProperty = model.getObjectProperty(OntologyClass.URI_NAMESPACE + "ContainProperty");
        feature = model.getObjectProperty(OntologyClass.URI_NAMESPACE + "SubEntity");
        evaluatedBy = model.getObjectProperty(OntologyClass.URI_NAMESPACE + "EvaluatedBy");
    }


    public Individual search(String hash) {
        Individual ind = this.model.getIndividual(OntologyClass.URI_NAMESPACE + hash);
        return ind;
    }


    public void setEntityName(String entityName) {
        instance.addProperty(this.name, entityName);
    }


    public void setHashID(String hashID) {
        instance.addProperty(this.hashID, hashID);
    }


    public void setProperty(String key, String value) {

        PropertyObject prop = new PropertyObject(this.model, key, this.hash);

        if (!prop.status) {
            prop.setKey(key);
            prop.setValue(value);
        }

        if (!this.model.listStatements(this.instance, this.containProperty, prop.instance).hasNext()) {
            RelationshipGenerator.setRelationship(this.containProperty, this.instance, prop.instance);
        }


    }

    public Feature setFeature(String name, Map<String, String> map) {

        Feature f = new Feature(this.model, name, this.hash);


        for (Map.Entry<String, String> m : map.entrySet()) {
            f.setProperty(m.getKey(), m.getValue());
        }
        if (!this.model.listStatements(this.instance, this.feature, f.instance).hasNext()) {
            RelationshipGenerator.setRelationship(this.feature, this.instance, f.instance);
        }

        return f;
    }

    public void setBaseScore(Individual baseScore){
        if (!this.model.listStatements(this.instance, this.evaluatedBy, baseScore).hasNext()) {
            RelationshipGenerator.setRelationship(this.evaluatedBy, this.instance, baseScore);
        }
    }


    public void makeThisMainEntityForCurrentReview(Review review){
        review.setMainEnity(this.instance);
    }
    public void save() {
        OntologyWriter.writeOntology(this.model);
    }

}
