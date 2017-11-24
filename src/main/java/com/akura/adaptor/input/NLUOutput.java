package com.akura.adaptor.input;

import java.util.ArrayList;
import java.util.UUID;

public class NLUOutput {

    public String reviewId;
    public String review;
    public Float reviewRating;
    public ArrayList<Entity> finalEntityTaggedList;
    public Specification specificationDto;

    public void replaceIdentifiers() {
        setIdentifiers();
        this.specificationDto.mainEntity.id = findIdFromFinalEntityTaggedList(this.specificationDto.mainEntity.text);

        ArrayList<Entity> relativeEntityList = new ArrayList<>();
        for (Entity ent : this.specificationDto.relativeEntityList) {
            ent.id = findIdFromFinalEntityTaggedList(ent.text);
            relativeEntityList.add(ent);
        }
        this.specificationDto.relativeEntityList = relativeEntityList;


        ArrayList<Relationship> specRelationshipDtoList = new ArrayList<>();
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
        this.specificationDto.specRelationshipDtoList = specRelationshipDtoList;

    }


    public String findIdFromFinalEntityTaggedList(String name) {
        String id = "";
        for (Entity ent : finalEntityTaggedList) {

            if ( ent.text.equals(name)) {
                id = ent.id;
            }
        }
        return id;
    }

    public Entity findEntityFromFinalEntityTaggedList(String name) {
        Entity entity = null;
        for (Entity ent : finalEntityTaggedList) {
            if ( ent.text.equals(name)) {
                entity = ent;
            }
        }

        return entity;

    }

    public void setIdentifiers(){
        for (int i = 0; i < finalEntityTaggedList.size(); i++) {
            Entity ent =  finalEntityTaggedList.get(i);
            ent.id = UUID.randomUUID() + "";

            finalEntityTaggedList.set(i,ent);
        }


    }
}
