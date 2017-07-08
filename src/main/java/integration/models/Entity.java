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
    /**
     * Create new Instance from class
     */
    public Entity(OntModel m, String name) {
        this.model = m;

        this.entityClass = (OntClass) this.model.getOntClass(OntologyClass.ENTITY);
        this.hash = HashGeneratorClass.generateHashForString(name,this.prefix);

        this.initProperties();
        Individual ind = this.search();

        if (ind == null) {
            this.instance = entityClass.createIndividual(OntologyClass.URI_NAMESPACE
                    + this.hash);
        } else {
            this.instance = ind;
        }

        this.setLiteralProperties(name, this.hash);
    }

    /**
     * Initialize class with Ontology Model
     *
     * @param m
     */
    public Entity(OntModel m) {
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(OntologyClass.ENTITY);
        this.initProperties();
    }

    private void initProperties() {
        name = model.getProperty(OntologyClass.URI_NAMESPACE + "EntityName");
        hashID = model.getProperty(OntologyClass.URI_NAMESPACE + "HashID");
    }

    private void setLiteralProperties(String entityName, String hash) {

        instance.addProperty(this.name, entityName);
        instance.addProperty(this.hashID, hash);

    }


    public void save() {
        UtilitiesClass.writeOntology(this.model);
    }


    public Individual search() {
        Individual ind = this.model.getIndividual(OntologyClass.URI_NAMESPACE + this.hash);
        return ind;
    }
}
