package com.akura.integration.models;

import com.akura.config.Config;
import com.akura.utility.HashGeneratorClass;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

/**
 * Class representing a Feature,
 */
public class Feature {

    public static String prefix = "FEATURE";

    public Individual instance;
    public OntClass entityClass;
    public String hash;
    public Boolean status;

    private Property name;
    private ObjectProperty featureProperty;
    private ObjectProperty evaluatedBy;
    private Property hashID;
    private OntModel model;

    public Feature(OntModel m) {
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(Config.FEATURE);
        this.initProperties();
    }

    public Feature(OntModel m, String name, String parentHash) {
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(Config.FEATURE);
        this.initProperties();

        this.hash = parentHash + "-" + HashGeneratorClass.generateHashForString(name, this.prefix);

        Individual ind = this.search(this.hash);
        if (ind == null) {
            this.instance = entityClass.createIndividual(Config.URI_NAMESPACE
                    + this.hash);
            this.setName(name);
            this.setHashID(this.hash);
            this.status = false;
        } else {
            this.status = true;
            this.instance = ind;
        }
    }

    /**
     * Method used to initialize the properties.
     */
    private void initProperties() {
        name = model.getProperty(Config.URI_NAMESPACE + "Name");
        featureProperty = model.getObjectProperty(Config.URI_NAMESPACE + "FeatureProperty");
        evaluatedBy = model.getObjectProperty(Config.URI_NAMESPACE + "EvaluatedBy");
        hashID = model.getProperty(Config.URI_NAMESPACE + "HashID");

    }

    /**
     * Method used to set the name of the feature.
     *
     * @param name - name of the feature.
     */
    public void setName(String name) {
        this.instance.addProperty(this.name, name);
    }

    /**
     * Method used to set the property of a feature.
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

        if (!this.model.listStatements(this.instance, this.featureProperty, prop.instance).hasNext()) {
            RelationshipGenerator.setRelationship(this.featureProperty, this.instance, prop.instance);
        }
    }

    /**
     * Method used to set the base score.
     *
     * @param baseScore - base score.
     */
    public void setBaseScore(Individual baseScore) {
        if (!this.model.listStatements(this.instance, this.evaluatedBy, baseScore).hasNext()) {
            RelationshipGenerator.setRelationship(this.evaluatedBy, this.instance, baseScore);
        }
    }

    /**
     * Method used to set the hash id of the feature.
     *
     * @param hashID - hash id.
     */
    public void setHashID(String hashID) {
        instance.addProperty(this.hashID, hashID);
    }

    /**
     * Method used to search feature by hash id.
     *
     * @param hash - hash id.
     * @return - individual instance.
     */
    public Individual search(String hash) {
        Individual ind = this.model.getIndividual(Config.URI_NAMESPACE + hash);
        return ind;
    }
}
