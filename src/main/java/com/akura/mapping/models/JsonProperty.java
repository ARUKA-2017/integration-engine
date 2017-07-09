package com.akura.mapping.models;


import com.akura.config.Config;
import com.akura.integration.models.RelationshipGenerator;
import com.akura.utility.HashGeneratorClass;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;

public class JsonProperty {
    public String key;
    public String value;

    public Individual instance;

    public void setObject(OntModel m, String parentHash, Individual parent) {

        String hash = parentHash + "-" + HashGeneratorClass.generateHashForString(this.key, "PROPERTY");
        OntClass clazz = m.getOntClass(Config.DYNAMIC_ONTOLOGY_URI + "Property");
        instance = clazz.createIndividual(Config.DYNAMIC_ONTOLOGY_URI
                + hash);

        instance.addProperty(m.getProperty(Config.DYNAMIC_ONTOLOGY_URI + "Key"), this.key);
        instance.addProperty(m.getProperty(Config.DYNAMIC_ONTOLOGY_URI + "Value"), this.value);

        ObjectProperty hasProp = m.getObjectProperty(Config.DYNAMIC_ONTOLOGY_URI + "HasProperty");

        if (!m.listStatements(parent, hasProp, this.instance).hasNext()) {
            RelationshipGenerator.setRelationship(hasProp, parent, this.instance);
        }
    }
}
