package com.akura.retrieval.models;

import com.akura.config.Config;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

/**
 * Class representing an PropertyObject of an entity.
 */
public class PropertyObject {

    private OntModel model;
    public Individual instance;

    private Property key;
    private Property value;


    public PropertyObject(OntModel m, Individual instance) {
        this.model = m;
        this.instance = instance;
        this.initProperties();
    }

    /**
     * Method used to initialize the property values.
     */
    private void initProperties() {
        key = model.getProperty(Config.URI_NAMESPACE + "Key");
        value = model.getProperty(Config.URI_NAMESPACE + "Value");
    }

    /**
     * Method used to get the key of the property.
     *
     * @return - key values of the property.
     */
    public String getKey() {
        return this.instance.getProperty(this.key).getLiteral().toString();
    }

    /**
     * Method used to get the value of the property.
     *
     * @return - value of the property.
     */
    public String getValue() {
        return this.instance.getProperty(this.value).getLiteral().toString();
    }
}
