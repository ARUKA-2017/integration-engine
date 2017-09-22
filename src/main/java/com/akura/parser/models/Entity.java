package com.akura.parser.models;

import com.akura.parser.config.Config;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Entity {

    public Map simpleTypes;
    public String name;
    public HashMap<String, ArrayList<Entity>> complexTypes;
    public HashMap<String, ArrayList<String>> simpleComplexTypes;
    public String classURI;
    public Individual instance;
    public UUID namespace = UUID.randomUUID();


    public Entity(String _key) {
        simpleTypes = new HashMap();
        complexTypes = new HashMap<>();
        simpleComplexTypes = new HashMap<>();
        name = _key;
    }

    public void addComplexType(String key, Entity ent) {

        if (complexTypes.get(key) != null) {
            complexTypes.get(key).add(ent);
        } else {
            ArrayList arr = new ArrayList();
            arr.add(ent);
            complexTypes.put(key, arr);
        }
    }

    public void addSimpleComplexType(String key, String val) {

        if (simpleComplexTypes.get(key) != null) {
            simpleComplexTypes.get(key).add(val);
        } else {
            ArrayList arr = new ArrayList();
            arr.add(val);
            simpleComplexTypes.put(key, arr);
        }
    }



    public void addSimpleType(String key, Object obj) {
        simpleTypes.put(key, obj);
    }


    public void saveToOntology() {
        if (name != null) {
            Ontology ont = new Ontology();
            classURI = ont.getClassName(name, simpleTypes, complexTypes, simpleComplexTypes);

            OntClass clazz = Ontology.getOntologyInstance().getOntClass(classURI);
            instance = clazz.createIndividual(Config.ONTOLOGY_URI + "--" + this.namespace.toString() + name.replace("#",""));


            // simple types
            for (Object key : simpleTypes.keySet()) {
               instance.addLiteral(ont.getProperty(key.toString()), simpleTypes.get(key.toString()).toString());
            }


            // complex types
            for (Object key : complexTypes.keySet()) {
                ArrayList<Entity> entities = complexTypes.get(key.toString());
                for (Entity ent : entities) {
                    instance.addProperty(ont.getProperty(key.toString()), ent.instance);
                }
            }

            for (Object key : simpleComplexTypes.keySet()) {
                ArrayList<String> values = simpleComplexTypes.get(key.toString());
                for (String val : values) {
                    instance.addLiteral(ont.getProperty(key.toString()),val);
                }
            }



        }


    }


}
