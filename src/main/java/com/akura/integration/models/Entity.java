package com.akura.integration.models;

import com.akura.config.Config;
import com.akura.utility.HashGeneratorClass;
import com.akura.utility.OntologyWriter;

import org.apache.jena.ontology.*;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;

import java.util.Map;

/**
 * Class representing an Entity.
 */
public class Entity {

    public static String prefix = "ENTITY";

    public Individual instance;
    public OntClass entityClass;
    public String hash;
    public OntModel model;

    private Property name;
    private Property hashID;
    private ObjectProperty containProperty;
    private ObjectProperty feature;
    private ObjectProperty evaluatedBy;

    public Entity(OntModel m, String name) {
        this.model = m;

        this.entityClass = (OntClass) this.model.getOntClass(Config.ENTITY);
        this.hash = HashGeneratorClass.generateHashForString(name, this.prefix);

        this.initProperties();

        Individual ind = this.search(this.hash);

        if (ind == null) {
            this.instance = entityClass.createIndividual(Config.URI_NAMESPACE
                    + this.hash);
            this.setEntityName(name);
            this.setHashID(this.hash);

        } else {
            this.instance = ind;
        }
    }

    public Entity(OntModel m) {
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(Config.ENTITY);
        this.initProperties();
    }

    /**
     * Method used to initialize the properties.
     */
    private void initProperties() {
        name = model.getProperty(Config.URI_NAMESPACE + "Name");
        hashID = model.getProperty(Config.URI_NAMESPACE + "HashID");

        containProperty = model.getObjectProperty(Config.URI_NAMESPACE + "ContainProperty");
        feature = model.getObjectProperty(Config.URI_NAMESPACE + "SubEntity");
        evaluatedBy = model.getObjectProperty(Config.URI_NAMESPACE + "EvaluatedBy");
    }

    /**
     * Method used to search entity by hash id.
     *
     * @param hash - hash id.
     * @return - individual.
     */
    public Individual search(String hash) {
        Individual ind = this.model.getIndividual(Config.URI_NAMESPACE + hash);
        return ind;
    }

    /**
     * Method used ot set the name of the entity.
     *
     * @param entityName - name of the entity.
     */
    public void setEntityName(String entityName) {
        instance.addProperty(this.name, entityName);
    }


    /**
     * Method used to set the hash id.
     *
     * @param hashID - hash id.
     */
    public void setHashID(String hashID) {
        instance.addProperty(this.hashID, hashID);
    }

    /**
     * Method used to set the property of the entity.
     *
     * @param key   - key of the property.
     * @param value - value of the property.
     */
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

    /**
     * Method used to set the feature of an entity,
     *
     * @param name - name of the entity.
     * @param map  - map.
     * @return - feature.
     */
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

    /**
     * Method used to set the base score of the entity.
     *
     * @param baseScore - individual instance.
     */
    public void setBaseScore(Individual baseScore) {
        if (!this.model.listStatements(this.instance, this.evaluatedBy, baseScore).hasNext()) {
            RelationshipGenerator.setRelationship(this.evaluatedBy, this.instance, baseScore);
        }
    }

    /**
     * Method used to this entity as a main entity for the current review.
     *
     * @param review - review.
     */
    public void makeThisMainEntityForCurrentReview(Review review) {
        review.setMainEnity(this.instance);
    }

    /**
     * Method used to save to the ontology.
     */
    public void save() {
        OntologyWriter.writeOntology(this.model);
    }
}
