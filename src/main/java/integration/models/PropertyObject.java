package integration.models;

import integration.OntologyClass;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;


public class PropertyObject {

    public static String prefix = "PROPERTY";

    public Individual instance;
    public OntClass entityClass;

    private Property key;
    private Property value;

    private OntModel model;

    public PropertyObject(OntModel m){
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(OntologyClass.ENTITY);
        this.initProperties();
    }


    private void initProperties() {
        key = model.getProperty(OntologyClass.URI_NAMESPACE + "Key");
        value = model.getProperty(OntologyClass.URI_NAMESPACE + "Value");
    }

    public Individual search(String hash) {
        Individual ind = this.model.getIndividual(OntologyClass.URI_NAMESPACE + hash);
        return ind;
    }
}
