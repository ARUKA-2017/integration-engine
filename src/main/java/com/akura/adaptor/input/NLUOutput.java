package com.akura.adaptor.input;

import com.akura.adaptor.resolver.EntityNameResolver;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Class representing a NLUOutput.
 */
public class NLUOutput {

    public String reviewId;
    public String review;
    public Float reviewRating;
    public ArrayList<Entity> finalEntityTaggedList;
    public Specification specificationDto;

    /**
     * Method used to replace the identifies of the entities.
     */
    public void replaceIdentifiers() {
        if (this.specificationDto != null) {
            setIdentifiers();
            if (this.specificationDto.mainEntity != null) {
                this.specificationDto.mainEntity.id = findIdFromFinalEntityTaggedList(this.specificationDto.mainEntity.text);
            }

            ArrayList<Entity> relativeEntityList = new ArrayList<>();
            if (this.specificationDto.relativeEntityList != null) {
                for (Entity ent : this.specificationDto.relativeEntityList) {
                    ent.id = findIdFromFinalEntityTaggedList(ent.text);

                    //resolve correct mobile name
                    String name = EntityNameResolver.getMobileName(ent.text);
                    if (name != null) {
                        ent.text = name;
                    }
                    relativeEntityList.add(ent);
                }
            }

            this.specificationDto.relativeEntityList = relativeEntityList;

            ArrayList<Relationship> specRelationshipDtoList = new ArrayList<>();
            if (this.specificationDto.specRelationshipDtoList != null) {
                for (Relationship rel : this.specificationDto.specRelationshipDtoList) {
                    rel.finalEntityTagDto.id = findIdFromFinalEntityTaggedList(rel.finalEntityTagDto.text);
//
//            ArrayList<Entity> featureMap = new ArrayList<>();
//            for(Entity ent: rel.featureMap){
//                ent.id = findIdFromFinalEntityTaggedList(ent.text);
//                featureMap.add(ent);
//            }
//            rel.featureMap = featureMap;
                    specRelationshipDtoList.add(rel);
                }
            }
            this.specificationDto.specRelationshipDtoList = specRelationshipDtoList;
        }
    }


    /**
     * Method used to find id from final entity tagged list.
     *
     * @param name - name of the entity.
     * @return - id of the entity.
     */
    public String findIdFromFinalEntityTaggedList(String name) {
        String id = "";
        for (Entity ent : finalEntityTaggedList) {

            if (ent.text.equals(name)) {
                id = ent.id;
            }
        }

        return id;
    }

    /**
     * Method used to find entity from final entity tagged list.
     *
     * @param name - name of the entity.
     * @return - entity.
     */
    public Entity findEntityFromFinalEntityTaggedList(String name) {
        Entity entity = null;
        for (Entity ent : finalEntityTaggedList) {
            if (ent.text.equals(name)) {
                entity = ent;
            }
        }

        return entity;
    }

    /**
     * Method used to find entity from relative taged list by name.
     *
     * @param name - name of the entity.
     * @return - entity.
     */
    public Entity findEntityFromRelativeTaggedListbyName(String name) {
        Entity entity = null;
        for (Entity ent : specificationDto.relativeEntityList) {
            if (ent.text.equals(name)) {
                entity = ent;
            }
        }

        return entity;
    }

    /**
     * Method used to set identifiers.
     */
    public void setIdentifiers() {
        for (int i = 0; i < finalEntityTaggedList.size(); i++) {
            Entity ent = finalEntityTaggedList.get(i);
            ent.id = UUID.randomUUID() + "";
            finalEntityTaggedList.set(i, ent);
        }
    }
}
