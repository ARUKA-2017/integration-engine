package com.akura.mapping.models;

import com.akura.config.Config;
import com.akura.utility.HashGeneratorClass;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;

/**
 * Class representing a JsonReviewInfo.
 */
public class JsonReviewInfo {
    public String user_name;
    public String email;
    public String comment;

    // TODO get rating to baseScore
    public Float rating;
    public String id;
    public JsonProperty[] property;

    public Individual instance;

    /**
     * Method used to set the object,
     *
     * @param m - ontology model.
     */
    public void setObject(OntModel m) {

        String hash = HashGeneratorClass.generateHashForString(this.id, "REVIEW");
        OntClass clazz = m.getOntClass(Config.DYNAMIC_ONTOLOGY_URI + "ReviewInfo");
        instance = clazz.createIndividual(Config.DYNAMIC_ONTOLOGY_URI
                + hash);

        instance.addProperty(m.getProperty(Config.DYNAMIC_ONTOLOGY_URI + "UserName"), this.user_name);
        instance.addProperty(m.getProperty(Config.DYNAMIC_ONTOLOGY_URI + "Comment"), this.comment);
        instance.addProperty(m.getProperty(Config.DYNAMIC_ONTOLOGY_URI + "Email"), this.email);

        // set properties
        //TODO not in the dynamic ontology structure so Ignoring additional details for now
    }

    /**
     * Method used to search by hash id.
     *
     * @param hash - hash id.
     * @param m    - ontology model.
     * @return - Individual.
     */
    public Individual search(String hash, OntModel m) {
        Individual ind = m.getIndividual(Config.DYNAMIC_ONTOLOGY_URI + hash);
        return ind;
    }
}
