package integration.models;


import integration.HashGeneratorClass;
import integration.UtilitiesClass;
import org.apache.jena.ontology.*;
import integration.OntologyClass;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Property;



public class Entity {

    public static String prefix = "ENTITY";

    public Individual instance;
    public OntClass entityClass;

    private OntModel model;
    private Property name;
    private Property hashID;

    private String hash;

    public Entity(OntModel m, String name) {
        this.model = m;

        this.entityClass = (OntClass) this.model.getOntClass(OntologyClass.ENTITY);
        this.hash = HashGeneratorClass.generateHashForString(name,this.prefix);
        // 111111

        this.initProperties();

        Individual ind = this.search(this.hash);

        if (ind == null) {
            this.instance = entityClass.createIndividual(OntologyClass.URI_NAMESPACE
                    + this.hash);
        } else {
            this.instance = ind;
        }

        this.setLiteralProperties(name);
    }


    public Entity(OntModel m) {
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(OntologyClass.ENTITY);
        this.initProperties();
    }

    private void initProperties() {
        name = model.getProperty(OntologyClass.URI_NAMESPACE + "EntityName");
        hashID = model.getProperty(OntologyClass.URI_NAMESPACE + "HashID");
    }

    private void setLiteralProperties(String entityName) {

        this.setEntityName(entityName);
        this.setHashID(this.hash);

    }



    public void save() {
        UtilitiesClass.writeOntology(this.model);
    }


    public Individual search(String hash) {
        Individual ind = this.model.getIndividual(OntologyClass.URI_NAMESPACE + hash);
        return ind;
    }


    public void setEntityName(String entityName){
        instance.addProperty(this.name, entityName);
    }


    public void setHashID(String hashID){
        instance.addProperty(this.hashID, hashID);
    }


    public void setProperty(Individual range){
        RelationshipGenerator.setRelationship(this.model,"ContainProperty",this.instance, range);
    }

    public void setSubEntity(Individual range){
        RelationshipGenerator.setRelationship(this.model,"SubEntity",this.instance, range);
    }


}
