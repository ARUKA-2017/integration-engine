package com.akura.parser.models;

import com.akura.parser.config.Config;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Entity {

    public Map simpleTypes;
    public String name;
    public HashMap<String, ArrayList<Entity>> complexTypes;
    public String classURI;
    public Individual instance;
    public UUID namespace = UUID.randomUUID();


    public Entity(String _key) {
        simpleTypes = new HashMap();
        complexTypes = new HashMap<>();
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


    public void addSimpleType(String key, Object obj) {
        simpleTypes.put(key, obj);
    }


    public void saveToOntology() {
        if (name != null) {
            Ontology ont = new Ontology();
            classURI = ont.getClassName(name, simpleTypes, complexTypes);
            OntModel m = Ontology.getOntologyInstance();
            OntClass clazz = m.getOntClass(classURI);

            instance = clazz.createIndividual(Config.ONTOLOGY_URI + "--" + this.namespace.toString() + name);

            // simple types
            for (Object key : simpleTypes.keySet()) {
                instance.addProperty(ont.getProperty(key.toString()), this.name);
            }

            // complex types
            for (Object key : complexTypes.keySet()) {

                ArrayList<Entity> entities = complexTypes.get(key.toString());

                for (Entity ent : entities) {
                    instance.addProperty(ont.getProperty(key.toString()), ent.instance);
                }
            }

        }


    }


}
