package com.akura.integration.dynamic;


import com.akura.integration.models.Entity;
import com.akura.integration.models.Review;
import com.akura.integration.models.Reviewer;
import com.oracle.webservices.internal.api.message.PropertySet;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdf.model.impl.StatementImpl;

import java.util.ArrayList;

public class ReviewInfo {

    private OntModel model;
    public OntClass entityClass;
    public Individual instance;
    public ArrayList<Property> properties;


    public ReviewInfo(OntModel m, Individual instance) {
        this.model = m;
        this.instance = instance;
        this.getProperties();
    }


    public ArrayList<Property> getProperties() {

        this.properties = IntegrationHelper.listPropertiesToArrayList(this.instance);
        return this.properties;
    }

    public Reviewer createReviewer(OntModel m) throws Exception {

        String email = IntegrationHelper.getLiteralPropertyValue(this.properties, "Email", this.instance);
        String username = IntegrationHelper.getLiteralPropertyValue(this.properties, "UserName", this.instance);

        if (email != null && username != null) {
            return new Reviewer(m, email, username);

        } else {
            throw new Exception("email or username null in " + this.instance.getLocalName());
        }

    }

    public Review createReview(Reviewer r) throws Exception {

        String comment = IntegrationHelper.getLiteralPropertyValue(this.properties, "Comment", this.instance);

        if (comment == null) {
            throw new Exception("comment null " + this.instance.getLocalName());
        }
        return r.createReviewAndGetInstance(comment);
    }


    public Entity createMainEntity(OntModel stat, Review review) throws Exception {
        Individual DynamicEntityInstance = IntegrationHelper.getObjectPropertyValue(this.model,
                this.properties,
                "MainEntity",
                this.instance);

        DynamicEntity entity = new DynamicEntity(this.model, stat, DynamicEntityInstance, review);
        Entity ent = entity.getEntityObject(stat);


        // link this as the main entity with review
        ent.makeThisMainEntityForCurrentReview(review);
        return ent;
    }

}
