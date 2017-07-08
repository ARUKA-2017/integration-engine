package com.akura.retrieval.models;


import com.akura.config.Config;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

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

    private void initProperties() {
        key = model.getProperty(Config.URI_NAMESPACE + "Key");
        value = model.getProperty(Config.URI_NAMESPACE + "Value");
    }

    public String getKey() {
        return this.instance.getProperty(this.key).getLiteral().toString();
    }

    public String getValue() {
        return this.instance.getProperty(this.value).getLiteral().toString();
    }
}
