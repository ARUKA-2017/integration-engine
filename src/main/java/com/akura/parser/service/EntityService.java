package com.akura.parser.service;


import com.akura.parser.config.Config;
import com.akura.parser.models.Entity;
import com.akura.parser.models.Ontology;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;


import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class EntityService {

    public Map entityList;
    public OntModel m;

    public EntityService(Map entities){
        m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        entityList = entities;
        Entity ent = new Entity("rootClass", m);
        this.generateEntitiesFromObject(this.entityList, ent, ent.name);
    }


    public void generateEntitiesFromObject(Map _entityList, Entity _ent, String _key){

        Set keys = _entityList.keySet();

        Entity ent = new Entity(_key, m);

        for(Object key : keys){

            // simple type key value or complex time
            if(_entityList.get(key.toString()) != null && resolveType(_entityList.get(key.toString()),
                    ent , key.toString(),false)) {
                ent.addSimpleType(key.toString(),_entityList.get(key.toString()));
            }
        }

        if(_ent != null) {
            _ent.addComplexType(_key, ent);
        }

        // here ent processes should be over. This is the ideal time to save ent
        ent.saveToOntology();

    }


    public void generateEntitiesFromArray(ArrayList _entityList, Entity ent, String key){

        for(Object obj : _entityList){

            resolveType(obj, ent, key, true);
        }
    }


    public Boolean resolveType(Object obj, Entity ent ,String key , boolean comesFromArray){

        if( obj.getClass().getName() == Config.arrayListClass){

            this.generateEntitiesFromArray((ArrayList) obj, ent ,key);
            return false;

        }else if( obj.getClass().getName() == Config.objectClass){

            generateEntitiesFromObject((Map) obj, ent ,key);
            return false;

        }else{
            if(comesFromArray){
                ent.addSimpleComplexType(key,obj.toString());
            }
            return true;
        }
    }


    public void saveOntology(){
        Ontology.saveOntologyFile(this.m);
    }

}
