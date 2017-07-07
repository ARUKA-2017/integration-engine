package integration.models;


import integration.HashGeneratorClass;
import integration.UtilitiesClass;
import org.apache.jena.ontology.*;
import integration.OntologyClass;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;

import java.io.FileNotFoundException;
import java.io.PrintStream;


public class Entity {


    public Individual instance;
    public OntClass entityClass;
    private OntModel model;


    private Property name;
    private Property hashID;


    /**
     * Create new Instance from class
     */
    public Entity(OntModel m, String name) {
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(OntologyClass.ENTITY);
        this.instance = entityClass.createIndividual(OntologyClass.URI_NAMESPACE
                + HashGeneratorClass.generateHashForString(name));

        this.instensiateProperties();
        this.setLiteralProperties(name,HashGeneratorClass.generateHashForString(name));
    }

    /**
     * Initialize class with Ontology Model
     *
     * @param m
     */
    public Entity(OntModel m) {
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(OntologyClass.ENTITY);
        this.instensiateProperties();
    }

    private void instensiateProperties(){
        name = model.getProperty(OntologyClass.URI_NAMESPACE + "EntityName");
        hashID = model.getProperty(OntologyClass.URI_NAMESPACE + "HashID");
    }

    public void setLiteralProperties(String entityName, String hash){

        instance.addProperty(this.name,entityName);
        instance.addProperty(this.hashID, hash);

    }


    public void save(){
        UtilitiesClass.writeOntology(this.model);
    }
}
