package com.akura.integration.models;

import com.akura.config.Config;
import com.akura.utility.HashGeneratorClass;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;


public class PropertyObject {

    public static String prefix = "PROPERTY";

    public Individual instance;
    public OntClass entityClass;

    private Property key;
    private Property value;
    private String hash;
    private OntModel model;

    // if true --> saved. if not --> not saved
    public Boolean status;

    public PropertyObject(OntModel m) {
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(Config.PROPERTY);
        this.initProperties();
    }

    public PropertyObject(OntModel m, String name, String parentHash) {
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(Config.PROPERTY);
        this.initProperties();

        this.hash = parentHash + "-" + HashGeneratorClass.generateHashForString(name, this.prefix);

        Individual ind = this.search(this.hash);
        if (ind == null) {
            this.instance = entityClass.createIndividual(Config.URI_NAMESPACE
                    + this.hash);
            this.status = false;
        } else {
            this.status = true;
            this.instance = ind;
        }
    }


    private void initProperties() {
        key = model.getProperty(Config.URI_NAMESPACE + "Key");
        value = model.getProperty(Config.URI_NAMESPACE + "Value");
    }

    public void setKey(String key) {
        this.instance.addProperty(this.key, key);
    }

    public void setValue(String value) {
        this.instance.addProperty(this.value, value);
    }


    public Individual search(String hash) {
        Individual ind = this.model.getIndividual(Config.URI_NAMESPACE + hash);
        return ind;
    }
}
