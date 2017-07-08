package com.akura.integration.models;


import com.akura.config.Config;
import com.akura.utility.HashGeneratorClass;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;

public class Review {
    public static String prefix = "REVIEWER";

    public Individual instance;
    public OntClass entityClass;


    private String hash;
    private OntModel model;

    private ObjectProperty measuredBy;
    private ObjectProperty mainEntity;
    private ObjectProperty betterThan;

    public Boolean status;

    public Review(OntModel m) {
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(Config.REVIEW);
        this.initProperties();
    }

    public Review(OntModel m, String comment) {
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(Config.REVIEW);
        this.initProperties();

        this.hash = HashGeneratorClass.generateHashForString(comment, this.prefix);

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
        measuredBy = model.getObjectProperty(Config.URI_NAMESPACE + "MeasuredBy");
        mainEntity = model.getObjectProperty(Config.URI_NAMESPACE + "MainEntity");
        betterThan = model.getObjectProperty(Config.URI_NAMESPACE + "BetterThan");
    }

    public void setMainEnity(Individual enity) {

        if (!this.model.listStatements(this.instance, this.mainEntity, enity).hasNext()) {
            RelationshipGenerator.setRelationship(this.mainEntity, this.instance, enity);
        }

    }

    public void setBaseScore(Individual baseScore){
        if (!this.model.listStatements(this.instance, this.measuredBy, baseScore).hasNext()) {
            RelationshipGenerator.setRelationship(this.measuredBy, this.instance, baseScore);
        }

    }


    public void setComparison(Individual comparison){
        if (!this.model.listStatements(this.instance, this.betterThan, comparison).hasNext()) {
            RelationshipGenerator.setRelationship(this.betterThan, this.instance, comparison);
        }

    }


    public Individual search(String hash) {
        Individual ind = this.model.getIndividual(Config.URI_NAMESPACE + hash);
        return ind;
    }
}
