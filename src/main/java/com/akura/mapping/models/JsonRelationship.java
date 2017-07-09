package com.akura.mapping.models;


import com.akura.config.Config;
import com.akura.integration.models.RelationshipGenerator;
import com.akura.utility.HashGeneratorClass;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntModel;

public class JsonRelationship {
    public String type;
    public String domain;
    public String range;

    private OntModel model;

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

    public void setMainEntity() {
        ObjectProperty prop = model.getObjectProperty(Config.DYNAMIC_ONTOLOGY_URI + "MainEntity");
        JsonReviewInfo js = new JsonReviewInfo();


        Individual inst1 = js.search(HashGeneratorClass.generateHashForString(this.domain, "REVIEW"), model);

        JsonEntity ent2 = new JsonEntity();
        Individual inst2 = ent2.search(HashGeneratorClass.generateHashForString(this.range, "ENTITY"), model);

        setRelationship(inst1, prop, inst2);
    }

    public void setFeature() {
        ObjectProperty prop = model.getObjectProperty(Config.DYNAMIC_ONTOLOGY_URI + "SubEntity");

        JsonEntity ent1 = new JsonEntity();
        Individual inst1 = ent1.search(HashGeneratorClass.generateHashForString(this.domain, "ENTITY"), model);

        JsonEntity ent2 = new JsonEntity();
        Individual inst2 = ent2.search(HashGeneratorClass.generateHashForString(this.range, "ENTITY"), model);

        setRelationship(inst1, prop, inst2);
    }

    public void setbetterThan() {
        ObjectProperty prop = model.getObjectProperty(Config.DYNAMIC_ONTOLOGY_URI + "BetterThan");
        JsonEntity ent1 = new JsonEntity();
        Individual inst1 = ent1.search(HashGeneratorClass.generateHashForString(this.domain, "ENTITY"), model);

        JsonEntity ent2 = new JsonEntity();
        Individual inst2 = ent2.search(HashGeneratorClass.generateHashForString(this.range, "ENTITY"), model);

        setRelationship(inst1, prop, inst2);
    }

    public void setRelationship(Individual domain, ObjectProperty prop, Individual range) {
        if (!model.listStatements(domain, prop, range).hasNext()) {
            RelationshipGenerator.setRelationship(prop, domain, range);
        }
    }
}
