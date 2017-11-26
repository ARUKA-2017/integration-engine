package com.akura.mapping.models;

import org.apache.jena.ontology.OntModel;

import java.util.ArrayList;

/**
 * Class representing a JsonResponse.
 */
public class JsonResponse {
    public JsonReviewInfo review_info;
    public ArrayList<JsonEntity> entities;
    public ArrayList<JsonRelationship> relationships;

    public JsonResponse() {
        this.review_info = new JsonReviewInfo();
        this.entities = new ArrayList<>();
        this.relationships = new ArrayList<>();
    }

    /**
     * Method used to set entities.
     *
     * @param m - ontology  model.
     */
    public void setEntities(OntModel m) {
        for (JsonEntity js : entities) {
            js.setObject(m);
        }
    }

    /**
     * Method used to set the relationship.
     *
     * @param m - ontology model.
     */
    public void setRelationships(OntModel m) {
        for (JsonRelationship js : relationships) {
            js.setRelationship(m);
        }
    }

    /**
     * Method used to set the review.
     *
     * @param m - ontology model.
     */
    public void setReview(OntModel m) {
        System.out.println(this.review_info);
        this.review_info.setObject(m);
    }


    /**
     * Methos used to set all the relevant properties.
     *
     * @param m - ontology model.
     */
    public void setAll(OntModel m) {
        setReview(m);
        setEntities(m);
        setRelationships(m);
    }
}
