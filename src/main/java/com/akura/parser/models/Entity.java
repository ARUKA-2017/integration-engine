package com.akura.parser.models;

import com.akura.parser.config.Config;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;

import java.util.*;

/**
 * Class representing a Entity.
 */
public class Entity {

    public Map simpleTypes;
    public String name;
    public HashMap<String, ArrayList<Entity>> complexTypes;
    public HashMap<String, ArrayList<String>> simpleComplexTypes;
    public HashMap<String, Boolean> complexTypesPureSatatus;
    public String classURI;
    public Individual instance;
    public UUID namespace = UUID.randomUUID();
    public OntModel m;

    public Entity(String _key, OntModel m) {
        this.m = m;

        simpleTypes = new HashMap();
        complexTypes = new HashMap<>();
        simpleComplexTypes = new HashMap<>();
        complexTypesPureSatatus = new HashMap<>();

        name = _key;
    }

    /**
     * Method used to add complex type of objects.
     *
     * @param key - key value.
     * @param ent - entity.
     */
    public void addComplexType(String key, Entity ent) {

        // NOTE: keyname is used instead of key to identify common classes with seperate key names. To revert back just replace keyname with key
        String keyName = this.m.getOntClass(ent.classURI).getLocalName();

        if (complexTypes.get(keyName) != null) {
            complexTypes.get(keyName).add(ent);

        } else {
            ArrayList arr = new ArrayList();
            arr.add(ent);

            complexTypes.put(keyName, arr);

        }
    }

    /**
     * Method used to add simple complex type values.
     *
     * @param key - key.
     * @param val - value.
     */
    public void addSimpleComplexType(String key, String val) {

        if (simpleComplexTypes.get(key) != null) {
            simpleComplexTypes.get(key).add(val);
        } else {
            ArrayList arr = new ArrayList();
            arr.add(val);
            simpleComplexTypes.put(key, arr);
        }
    }

    /**
     * Method used to add simple type.
     *
     * @param key - key.
     * @param obj - object.
     */
    public void addSimpleType(String key, Object obj) {
        simpleTypes.put(key, obj);
    }

    /**
     * Method used to save the ontology.
     */
    public void saveToOntology() {
        if (name != null) {
            Ontology ont = new Ontology(this.m);
            System.out.println("NAME: " + name);

            classURI = ont.getClassName(name, simpleTypes, complexTypes, simpleComplexTypes);

            System.out.println(classURI);
            OntClass clazz = this.m.getOntClass(classURI);
            instance = clazz.createIndividual(Config.ONTOLOGY_URI + "--" + this.namespace.toString() + name.replace("#", ""));


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
                    instance.addLiteral(ont.getProperty(key.toString()), val);
                }
            }

        }
    }

    /**
     * Method used to change the class name.
     *
     * @param newName - new name of the class.
     */
    public void changeClassName(String newName) {

        name = newName;
        if (instance != null) {
            instance.remove();
            instance = null;
        }

        if (classURI != null) {
            OntClass clazz = this.m.getOntClass(classURI);

            if (clazz != null) {
                clazz.remove();
            }
            classURI = null;
        }
    }
}
