package com.akura.integration.models;


import com.akura.config.Config;
import com.akura.utility.HashGeneratorClass;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

public class Reviewer {
    public static String prefix = "REVIEWER";

    public Individual instance;
    public OntClass entityClass;

    private Property email;
    private Property username;

    private ObjectProperty providesProperty;

    private String hash;
    private OntModel model;

    public Boolean status;

    public Reviewer(OntModel m) {
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(Config.REVIEWER);
        this.initProperties();
    }

    public Reviewer(OntModel m, String email, String username) {
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(Config.REVIEWER);
        this.initProperties();

        this.hash = HashGeneratorClass.generateHashForString(email, this.prefix);

        Individual ind = this.search(this.hash);
        if (ind == null) {
            this.instance = entityClass.createIndividual(Config.URI_NAMESPACE
                    + this.hash);

            this.setEmail(email);
            this.setUsername(username);

            this.status = false;
        } else {
            this.status = true;
            this.instance = ind;
        }
    }


    private void initProperties() {
        username = model.getProperty(Config.URI_NAMESPACE + "UserName");
        email = model.getProperty(Config.URI_NAMESPACE + "Email");

        providesProperty = model.getObjectProperty(Config.URI_NAMESPACE + "Provides");

    }

    public void setUsername(String username) {
        this.instance.addProperty(this.username, username);
    }

    public void setEmail(String email) {
        this.instance.addProperty(this.email, email);
    }

    public void setReview(Individual review) {

        if (!this.model.listStatements(this.instance, this.providesProperty, review).hasNext()) {
            RelationshipGenerator.setRelationship(this.providesProperty, this.instance, review);
        }

    }


    public Review createReviewAndGetInstance(String comment){
        Review review = new Review(this.model, comment);
        this.setReview(review.instance);
        return review;
    }

    public Individual search(String hash) {
        Individual ind = this.model.getIndividual(Config.URI_NAMESPACE + hash);
        return ind;
    }
}
