package com.akura.mapping.models;


import org.apache.jena.ontology.OntModel;

import java.util.ArrayList;

public class JsonResponse {
    public JsonReviewInfo review_info;
    public ArrayList<JsonEntity> entities;
    public ArrayList<JsonRelationship> relationships;


    public void setEntities(OntModel m) {
        for (JsonEntity js : entities) {
            js.setObject(m);
        }
    }

    public void setRelationships(OntModel m) {
        for (JsonRelationship js : relationships) {
            js.setRelationship(m);
        }
    }

    public void setReview(OntModel m){
        this.review_info.setObject(m);
    }


    public void setAll(OntModel m){
        setReview(m);
        setEntities(m);
        setRelationships(m);

    }
}
