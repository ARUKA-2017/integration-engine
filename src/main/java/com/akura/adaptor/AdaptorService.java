package com.akura.adaptor;

import com.akura.adaptor.input.Entity;
import com.akura.adaptor.input.NLUOutput;
import com.akura.adaptor.input.Relationship;
import com.akura.mapping.models.JsonEntity;
import com.akura.mapping.models.JsonProperty;
import com.akura.mapping.models.JsonRelationship;
import com.akura.mapping.models.JsonResponse;

/**
 * Class representing an AdaptorService.
 */
public class AdaptorService {

    public NLUOutput source;
    public JsonResponse target;
    public Boolean mainEntityStatus = false;

    public AdaptorService(NLUOutput source) {
        this.source = source;
        target = new JsonResponse();
    }

    /**
     * Method used to convert the NLU output.
     */
    public void convert() {
        setReviewInfo();
        setJSONEntities();
        setRelationships();
    }

    /**
     * Method used set the review info.
     */
    public void setReviewInfo() {

        target.review_info.id = source.reviewId;
        target.review_info.comment = source.review;
        target.review_info.email = "nilesh.jayanandana@yahoo.com";
        target.review_info.rating = source.reviewRating;
        target.review_info.user_name = "nilesh jayanandana";
    }

    /**
     * Method used to the set the JSON entities.
     */
    public void setJSONEntities() {
        for (Entity ent : this.source.specificationDto.relativeEntityList) {
            addJSONEntity(ent);
        }
    }

    /**
     * Method used to add JSON entity.
     *
     * @param ent - entity.
     */
    public void addJSONEntity(Entity ent) {

        JsonEntity jEnt = new JsonEntity();
        //TODO: salience used instead of sentiment because sentiment comes as zero
        jEnt.base_score = ent.salience;

        jEnt.name = ent.text;
        jEnt.id = ent.id;
        jEnt.property = new JsonProperty[]{};

        this.target.entities.add(jEnt);
    }

    /**
     * Method used to set the relationship.
     */
    public void setRelationships() {
        setMainEntity();
        setFeatures();
    }

    /**
     * Method used to set the features.
     */
    public void setFeatures() {
        for (Relationship rel : this.source.specificationDto.specRelationshipDtoList) {
            // get entities of the hashmap
            if (this.source.findEntityFromRelativeTaggedListbyName(rel.finalEntityTagDto.text) != null) {
                for (String name : rel.featureMap.keySet()) {

                    Entity ent = this.source.findEntityFromFinalEntityTaggedList(name);
                    if (ent != null) {
                        // if entity is not there in the entitylist, add it
                        addMissingEntity(ent);

                        JsonRelationship featureRelationship = new JsonRelationship();
                        featureRelationship.type = "Feature";
                        featureRelationship.domain = rel.finalEntityTagDto.id;
                        featureRelationship.range = ent.id;

                        this.target.relationships.add(featureRelationship);
                    }
                }
            }
        }
    }


    /**
     * Method used to set the maing entity.
     */
    public void setMainEntity() {
        for (Entity ent : this.source.specificationDto.relativeEntityList) {
            if (ent.id.equals(this.source.specificationDto.mainEntity.id)) {
                JsonRelationship featureRelationship = new JsonRelationship();
                featureRelationship.type = "MainEntity";
                featureRelationship.domain = this.source.reviewId;
                featureRelationship.range = this.source.specificationDto.mainEntity.id;
                this.target.relationships.add(featureRelationship);
                this.mainEntityStatus = true;
            }
        }
    }

    /**
     * Method used to set better than instances.
     */
    public void setBetterThan() {
        // todo:
    }

    /**
     * Method used add the missing entity.
     *
     * @param feature - feature entity.
     */
    public void addMissingEntity(Entity feature) {
        System.out.println("Adding missing entity " + feature.text);
        Boolean missing = true;
        for (JsonEntity ent : this.target.entities) {
            if (ent.id.equals(feature.id)) {
                missing = false;
            }
        }
        if (missing) {
            addJSONEntity(feature);
        }
    }
}
