package com.akura.mapping.models;

import com.akura.config.Config;
import com.akura.integration.models.RelationshipGenerator;
import com.akura.utility.HashGeneratorClass;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntModel;

/**
 * Class representing a JsonRelationship.
 */
public class JsonRelationship {
    public String type;
    public String domain;
    public String range;

    private OntModel model;

    /**
     * Method used to set the relationship.
     *
     * @param m - ontology model.
     */
    public void setRelationship(OntModel m) {
        model = m;

        switch (type) {
            case "MainEntity":
                setMainEntity();
                break;
            case "Feature":
                setFeature();
                break;
            case "BetterThan":
                setbetterThan();
                break;
        }
    }

    /**
     * Method used to set the main entity.
     */
    public void setMainEntity() {
        ObjectProperty prop = model.getObjectProperty(Config.DYNAMIC_ONTOLOGY_URI + "MainEntity");
        JsonReviewInfo js = new JsonReviewInfo();


        Individual inst1 = js.search(HashGeneratorClass.generateHashForString(this.domain, "REVIEW"), model);

        JsonEntity ent2 = new JsonEntity();
        Individual inst2 = ent2.search(HashGeneratorClass.generateHashForString(this.range, "ENTITY"), model);

        setRelationship(inst1, prop, inst2);
    }

    /**
     * Method used to set the feature of an entity.
     */
    public void setFeature() {
        ObjectProperty prop = model.getObjectProperty(Config.DYNAMIC_ONTOLOGY_URI + "SubEntity");

        JsonEntity ent1 = new JsonEntity();
        Individual inst1 = ent1.search(HashGeneratorClass.generateHashForString(this.domain, "ENTITY"), model);

        JsonEntity ent2 = new JsonEntity();
        Individual inst2 = ent2.search(HashGeneratorClass.generateHashForString(this.range, "ENTITY"), model);

        setRelationship(inst1, prop, inst2);
    }

    /**
     * Method used to set the better than entity.
     */
    public void setbetterThan() {
        ObjectProperty prop = model.getObjectProperty(Config.DYNAMIC_ONTOLOGY_URI + "BetterThan");
        JsonEntity ent1 = new JsonEntity();
        Individual inst1 = ent1.search(HashGeneratorClass.generateHashForString(this.domain, "ENTITY"), model);

        JsonEntity ent2 = new JsonEntity();
        Individual inst2 = ent2.search(HashGeneratorClass.generateHashForString(this.range, "ENTITY"), model);

        setRelationship(inst1, prop, inst2);
    }

    /**
     * Method used set the relationship.
     *
     * @param domain - domain of a relationship.
     * @param prop   - property of relationship.
     * @param range  - range of a relationship.
     */
    public void setRelationship(Individual domain, ObjectProperty prop, Individual range) {
        if (!model.listStatements(domain, prop, range).hasNext()) {
            RelationshipGenerator.setRelationship(prop, domain, range);
        }
    }
}
