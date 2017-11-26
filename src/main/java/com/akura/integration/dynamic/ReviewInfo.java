package com.akura.integration.dynamic;

import com.akura.integration.models.Entity;
import com.akura.integration.models.Review;
import com.akura.integration.models.Reviewer;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

import java.util.ArrayList;

/**
 * Class representing a ReviewInfo.
 */
public class ReviewInfo {

    private OntModel dynamic;
    public OntClass entityClass;
    public Individual instance;
    public ArrayList<Property> properties;

    public ReviewInfo(OntModel m, Individual instance) {
        this.dynamic = m;
        this.instance = instance;
        this.getProperties();
    }

    /**
     * Method used to get the properties.
     *
     * @return - list of properties.
     */
    public ArrayList<Property> getProperties() {

        this.properties = IntegrationHelper.listPropertiesToArrayList(this.instance);
        return this.properties;
    }

    /**
     * Method used to create a reviewer.
     *
     * @param m - ontology model.
     * @return - Reviewer.
     * @throws Exception - exception.
     */
    public Reviewer createReviewer(OntModel m) throws Exception {

        String email = IntegrationHelper.getLiteralPropertyValue(this.properties, "Email", this.instance);
        String username = IntegrationHelper.getLiteralPropertyValue(this.properties, "UserName", this.instance);

        if (email != null && username != null) {
            return new Reviewer(m, email, username);

        } else {
            throw new Exception("email or username null in " + this.instance.getLocalName());
        }
    }

    /**
     * Method used create review.
     *
     * @param r - reviewer instance.
     * @return - Review.
     * @throws Exception -exception.
     */
    public Review createReview(Reviewer r) throws Exception {

        String comment = IntegrationHelper.getLiteralPropertyValue(this.properties, "Comment", this.instance);

        if (comment == null) {
            throw new Exception("comment null " + this.instance.getLocalName());
        }
        return r.createReviewAndGetInstance(comment);
    }

    /**
     * Method used create the main entity.
     *
     * @param stat   - static ontology model.
     * @param review - review instnce.
     * @return - entity.
     * @throws Exception - exception.
     */
    public Entity createMainEntity(OntModel stat, Review review) throws Exception {
        Individual DynamicEntityInstance = IntegrationHelper.getObjectPropertyValue(this.dynamic,
                this.properties,
                "MainEntity",
                this.instance);

        DynamicEntity entity = new DynamicEntity(this.dynamic, stat, DynamicEntityInstance, review);
        Entity ent = entity.getEntityObject(stat);


        // link this as the main entity with review
        ent.makeThisMainEntityForCurrentReview(review);
        return ent;
    }
}
