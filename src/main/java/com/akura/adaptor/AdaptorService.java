package com.akura.adaptor;

import com.akura.adaptor.input.Entity;
import com.akura.adaptor.input.NLUOutput;
import com.akura.adaptor.input.Relationship;
import com.akura.mapping.models.JsonEntity;
import com.akura.mapping.models.JsonRelationship;
import com.akura.mapping.models.JsonResponse;

public class AdaptorService {

    public NLUOutput source;
    public JsonResponse target;

    public AdaptorService(NLUOutput source) {


        this.source = source;
        target = new JsonResponse();
    }

    public void convert() {
        setReviewInfo();
        setJSONEntities();
        setRelationships();
    }

    public void setReviewInfo() {
        target.review_info.id = source.reviewId;
        target.review_info.comment = source.review;
        target.review_info.email = "";
        target.review_info.rating = source.reviewRating;
        target.review_info.user_name = "";
    }

    public void setJSONEntities() {
        for (Entity ent : this.source.specificationDto.relativeEntityList) {
            addJSONEntity(ent);
        }
    }

    public void addJSONEntity(Entity ent) {

        JsonEntity jEnt = new JsonEntity();
        //TODO: this comes empty
        jEnt.base_score = ent.sentiment;

        jEnt.name = ent.text;
        jEnt.id = ent.id;

        this.target.entities.add(jEnt);
    }
    public void setRelationships() {
        setFeatures();
    }



    public void setFeatures() {
        for (Relationship rel : this.source.specificationDto.specRelationshipDtoList) {
            // get entities of the hashmap
            for (String name : rel.featureMap.keySet()) {

                Entity ent = this.source.findEntityFromFinalEntityTaggedList(name);
                if(ent != null) {
                    // if entity is not there in the entitylist, add it
                    addMissingEntity(ent);

                    JsonRelationship featureRelationship = new JsonRelationship();
                    featureRelationship.type = "Feature";
                    featureRelationship.domain = rel.finalEntityTagDto.id;
                    featureRelationship.range = ent.id;
                }
            }
        }
    }



    public void addMissingEntity(Entity feature) {

        Boolean missing = true;
        for (JsonEntity ent : this.target.entities) {
            if (ent.id == feature.id) {
                missing = false;
            }
        }
        if (missing) {
            addJSONEntity(feature);
        }
    }
}
