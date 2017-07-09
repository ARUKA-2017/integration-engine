package com.akura.mapping.models;


import com.akura.config.Config;
import com.akura.utility.HashGeneratorClass;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;

public class JsonEntity {
    public String id;
    public String name;
    public Float base_score;
    public JsonProperty[] property;

    public Individual instance;
    public String hash;

    public void setObject(OntModel m) {

        hash = HashGeneratorClass.generateHashForString(this.id, "ENTITY");
        OntClass clazz = m.getOntClass(Config.DYNAMIC_ONTOLOGY_URI + "Entity");
        Individual ind = this.search(hash, m);

        if (ind == null) {
            instance = clazz.createIndividual(Config.DYNAMIC_ONTOLOGY_URI
                    + hash);
            this.setProperties(m);
            this.setDynamicProperties(m);
        } else {
            instance = ind;
        }

    }

    public void setProperties(OntModel m) {
        instance.addProperty(m.getProperty(Config.DYNAMIC_ONTOLOGY_URI + "Name"), this.name);
        instance.addLiteral(m.getProperty(Config.DYNAMIC_ONTOLOGY_URI + "BaseScore"), this.base_score);
    }

    public Individual search(String hash, OntModel m) {
        Individual ind = m.getIndividual(Config.DYNAMIC_ONTOLOGY_URI + hash);
        return ind;
    }

    public void setDynamicProperties(OntModel m) {
        for (JsonProperty prop : property) {
            prop.setObject(m, this.hash, this.instance);
        }
    }

}
