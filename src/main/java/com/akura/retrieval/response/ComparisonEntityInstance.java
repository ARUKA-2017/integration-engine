package com.akura.retrieval.response;


import com.akura.retrieval.models.Entity;
import com.akura.retrieval.models.Feature;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;

public class ComparisonEntityInstance {
    public String name;
    public String id;
    public Integer count;


    public ComparisonEntityInstance(){

    }

    public ComparisonEntityInstance(OntModel m, Individual entityOrFeatureInstance){

        if(entityOrFeatureInstance.getOntClass().getLocalName().equals("Entity")){
            Entity ent = new Entity(m,entityOrFeatureInstance);
            this.name = ent.getName();
            this.id = ent.getHash();

        }else{
            Feature f = new Feature(m,entityOrFeatureInstance);
            this.name = f.getEntityName();
            this.id = f.getEntityHash();
        }

    }
}
