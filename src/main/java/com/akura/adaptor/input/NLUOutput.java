package com.akura.adaptor.input;

import java.util.ArrayList;

public class NLUOutput{

    public String reviewId;
    public String review;
    public Float reviewRating;
    public ArrayList<Entity> finalEntityTaggedList;
    public Specification specificationDto;

    public void replaceIdentifiers(){
        this.specificationDto.mainEntity.id = findIdFromFinalEntityTaggedList(this.specificationDto.mainEntity.text);

        ArrayList<Entity> relativeEntityList = new ArrayList<>();
        for(Entity ent: this.specificationDto.relativeEntityList){
            ent.id = findIdFromFinalEntityTaggedList(ent.text);
            relativeEntityList.add(ent);
        }
        this.specificationDto.relativeEntityList = relativeEntityList;


        ArrayList<Relationship> specRelationshipDtoList = new ArrayList<>();
        for(Relationship rel: this.specificationDto.specRelationshipDtoList){
            rel.finalEntityTagDto.id = findIdFromFinalEntityTaggedList(rel.finalEntityTagDto.text);

            ArrayList<Entity> featureMap = new ArrayList<>();
            for(Entity ent: rel.featureMap){
                ent.id = findIdFromFinalEntityTaggedList(ent.text);
                featureMap.add(ent);
            }
            rel.featureMap = featureMap;
            specRelationshipDtoList.add(rel);
        }
        this.specificationDto.specRelationshipDtoList = specRelationshipDtoList;

    }


    public String findIdFromFinalEntityTaggedList(String name){
        String id = "";
        for(Entity ent: finalEntityTaggedList){
            if(ent.text == name){
                id = ent.id;
            }
        }
        return id;
    }
}
